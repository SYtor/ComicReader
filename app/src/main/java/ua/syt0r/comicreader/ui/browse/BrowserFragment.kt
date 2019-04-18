package ua.syt0r.comicreader.ui.browse

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.util.Utils
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.ui.viewer.ViewerActivity
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

        val viewModel = ViewModelProviders.of(this).get(BrowseViewModel::class.java)

        val adapter = FolderAdapter(viewModel)
        adapter.currentFolderView = root.findViewById(R.id.parent_folder_text)
        adapter.progressBar = root.findViewById(R.id.progress)
        adapter.init(context!!)

        adapter.onFileSelectListener = object : FolderAdapter.OnFileSelectListener {
            override fun onFileSelected(dir: File, file: File) {
                val intent = Intent(context, ViewerActivity::class.java)
                intent.data = Uri.fromFile(file)
                context?.startActivity(intent)
            }
        }

        adapter.onFileLongClickListener = object : FolderAdapter.OnFileLongClickListener {
            override fun onFileLongClick(file: File) {
                bgScope.launch {
                    database.dbFileDao().insert(
                        DbFile(
                            0L, file.path, 0, Utils.getFileType(file),
                            1, System.currentTimeMillis()
                        )
                    )
                }
            }
        }

        recyclerView.adapter = adapter

        return root
    }

}