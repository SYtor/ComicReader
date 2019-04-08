package ua.syt0r.comicreader.ui.fragment.browse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzy.libp7zip.P7ZipApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.Utils
import ua.syt0r.comicreader.db.ComicDatabase
import ua.syt0r.comicreader.db.entity.History
import ua.syt0r.comicreader.ui.activity.viewer.ViewerActivity
import java.io.File

class BrowserFragment : Fragment() {

    private lateinit var database: ComicDatabase

    private val bgScope = CoroutineScope(Dispatchers.IO)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        database = ComicDatabase.getInstance(context.applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_browse, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = FolderAdapter()
        adapter.currentFolderView = root.findViewById(R.id.parent_folder_text)
        adapter.progressBar = root.findViewById(R.id.progress)
        adapter.init(context!!)

        adapter.onFileSelectListener = object : FolderAdapter.OnFileSelectListener {
            override fun onFileSelected(dir: File, file: File) {

                val dialog = AlertDialog.Builder(context!!)
                    .setTitle("Opening archive")
                    .setView(ProgressBar(context))
                    .create()
                dialog.show()

                bgScope.launch {

                    val intent = Intent(context, ViewerActivity::class.java)

                    val path = file.path
                    val indexOfLastSlash = path.lastIndexOf("/")
                    val nameWithExtension = path.substring(indexOfLastSlash + 1)
                    val dotIndex = nameWithExtension.lastIndexOf(".")
                    val extension = if (dotIndex == -1) "" else nameWithExtension.substring(Math.min(dotIndex + 1, nameWithExtension.length))

                    when(extension) {

                        in IMAGE_EXTENSIONS -> {

                            val files = getImagesFromFolder(dir)
                            intent.putExtra(ViewerActivity.TYPE_KEY, ViewerActivity.IMAGE_ARRAY_KEY)
                            intent.putExtra(ViewerActivity.IMAGE_ARRAY_KEY, files)
                            createHistoryRecord(file)
                            context?.startActivity(intent)

                        }

                        in ZIP_EXTENSIONS -> {
                            uiScope.launch { Toast.makeText(context,
                                "Extension $extension not supported", Toast.LENGTH_SHORT).show() }
                        }

                        in RAR_EXTENSIONS -> {

                            val externalFilesDirs = ContextCompat.getExternalFilesDirs(context!!, null)
                            val folder = externalFilesDirs.first()
                            val newDir = File(folder, file.name)

                            val isDirJustCreated = newDir.mkdir()
                            if (isDirJustCreated) {
                                val command = "7z x '$file' '-o$newDir' -aoa"
                                val result = P7ZipApi.executeCommand(command)
                                Log.d("Debug", "Command: $command")
                                Log.d("Debug", "Command result: $result")
                            }

                            val images = getImagesFromFolder(newDir)
                            intent.putExtra(ViewerActivity.TYPE_KEY, ViewerActivity.IMAGE_ARRAY_KEY)
                            intent.putExtra(ViewerActivity.IMAGE_ARRAY_KEY, images)

                            createHistoryRecord(file)

                            context?.startActivity(intent)

                        }

                        "pdf" -> {
                            intent.putExtra(ViewerActivity.TYPE_KEY, ViewerActivity.TYPE_PDF)
                            intent.putExtra(ViewerActivity.FILE_KEY, file.path)
                            context?.startActivity(intent)
                        }

                        else -> {
                            uiScope.launch { Toast.makeText(context,
                                "Extension $extension not supported", Toast.LENGTH_SHORT).show() }

                        }

                    }

                    dialog.dismiss()

                }

            }
        }

        recyclerView.adapter = adapter

        return root
    }

    private fun getImagesFromFolder(dir: File): ArrayList<String> {
        val files = ArrayList<String>()
        dir.listFiles().forEach {
            val p = it.path
            if (p.substring(p.lastIndexOf(".") + 1) in IMAGE_EXTENSIONS)
                files.add(it.path)
        }
        files.sortWith(Comparator { p1,p2 -> Utils.compareStringsWithNumbers(p1, p2) })

        files.forEach {
            Log.d("Debug", "image: $it")
        }

        return files
    }

    private fun createHistoryRecord(file: File) {
        val history = History()
        history.path = file.path
        history.readTime = System.currentTimeMillis()
        database.historyDao().insert(history)
    }

    companion object {
        val IMAGE_EXTENSIONS = arrayOf("jpg", "png", "jpeg", "bmp")
        val ZIP_EXTENSIONS = arrayOf("zip", "cbz")
        val RAR_EXTENSIONS = arrayOf("rar", "cbr")
    }

}