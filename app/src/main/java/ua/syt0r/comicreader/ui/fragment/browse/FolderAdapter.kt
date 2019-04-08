package ua.syt0r.comicreader.ui.fragment.browse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.Utils
import java.io.File

class FolderAdapter: RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    var rootDirectories: List<File>? = null
    var level = 0

    var currentRoot: File? = null
    var files: List<File>? = null

    var onFileSelectListener: OnFileSelectListener? = null

    val bgScope = CoroutineScope(Dispatchers.IO)
    val uiScope = CoroutineScope(Dispatchers.Main)

    lateinit var currentFolderView: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_folder_item, parent, false))
    }

    fun init(context: Context) {
        bgScope.launch {

            updateProgressBar(true)

            val externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
            val roots = ArrayList<File>()
            externalFilesDirs.forEach {
                val path = it.path
                roots.add(File(path.substring(0, path.indexOf("/Android/data/"))))
            }
            rootDirectories = roots

            updateAdapter()
            updateProgressBar(false)

        }
    }

    fun updateProgressBar(isLoading: Boolean) = uiScope.launch {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    fun updateAdapter() = uiScope.launch {
        currentFolderView.text = if (level == 0) "Memory" else currentRoot?.path
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (level == 0) return rootDirectories?.size ?: 0
        return files?.size?.plus(1) ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0 && level != 0) {
            holder.textView.text = "../"
            holder.imageView.setImageResource(R.drawable.ic_folder)
            return
        }

        val file = when(level) {
            0 -> rootDirectories?.getOrNull(position)
            else -> files?.getOrNull(position - 1)
        }

        file?.also {
            holder.textView.text = file.name
            holder.imageView.setImageResource(if (file.isFile) R.drawable.ic_file else R.drawable.ic_folder)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView : TextView = itemView.findViewById(R.id.text)
        val imageView : ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener {
                bgScope.launch {

                    updateProgressBar(true)

                    if (adapterPosition == 0 && level != 0) {

                        level -= 1
                        currentRoot = currentRoot?.parentFile
                        files = currentRoot?.listFiles()?.asList()?.sortedWith(Comparator { f1, f2 ->
                            Utils.compareFilesWithNumbers(f1, f2)
                        })
                        updateAdapter()

                    } else {

                        val file= if (level == 0) {
                            rootDirectories?.getOrNull(adapterPosition)
                        } else {
                            files?.getOrNull(adapterPosition - 1)
                        }

                        if (file?.isFile == true) {
                            uiScope.launch { onFileSelectListener?.onFileSelected(currentRoot!!, file) }
                        } else {
                            level += 1
                            currentRoot = file
                            files = currentRoot?.listFiles()?.asList()?.sortedWith(Comparator { f1, f2 ->
                                Utils.compareFilesWithNumbers(f1, f2)
                            })
                            updateAdapter()
                        }

                    }

                    updateProgressBar(false)

                }
            }
        }

    }

    interface OnFileSelectListener {
        fun onFileSelected(dir: File, file: File)
    }

}