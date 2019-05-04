package ua.syt0r.comicreader.ui.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.util.*
import java.io.File

class FolderAdapter : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    var files: List<File>? = null
    var isTopLevelDir = true

    var onFileSelectListener: OnFileSelectListener? = null
    var onFileLongClickListener: OnFileLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_folder_item, parent, false))
    }

    override fun getItemCount(): Int {
        return files?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        files?.getOrNull(position)?.also { file ->

            if (!isTopLevelDir && position == 0)
                holder.textView.text = "../"
            else
                holder.textView.text = file.name

            val type = getFileType(file)
            when(type) {
                FileType.FOLDER -> holder.imageView.setImageResource(R.drawable.ic_folder)
                FileType.IMAGE -> Picasso.get().load(file).fit().centerCrop().into(holder.imageView)
                FileType.PDF -> holder.imageView.setImageResource(R.drawable.ic_google_drive_pdf_file)
                else -> holder.imageView.setImageResource(R.drawable.ic_file)
            }
        }

    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        Picasso.get().cancelRequest(holder.imageView)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView : TextView = itemView.findViewById(R.id.text)
        val imageView : ImageView = itemView.findViewById(R.id.image)

        init {

            itemView.setOnClickListener {
                files?.getOrNull(adapterPosition)?.also { file ->
                    onFileSelectListener?.onFileSelected(!isTopLevelDir && adapterPosition == 0,file)
                }
            }

            itemView.setOnLongClickListener {

                files?.getOrNull(adapterPosition)?.also { file ->

                    AlertDialog.Builder(itemView.context)
                        .setTitle(R.string.pin)
                        .setMessage(R.string.pin_add_message)
                        .setPositiveButton(R.string.pin){ _,_ -> onFileLongClickListener?.onFileLongClick(file) }
                        .setNegativeButton(R.string.cancel){ _,_ -> }
                        .create()
                        .show()

                }

                return@setOnLongClickListener true
            }

        }

    }

    interface OnFileSelectListener {
        fun onFileSelected(isUpDirSelected: Boolean, file: File)
    }

    interface OnFileLongClickListener {
        fun onFileLongClick(file: File)
    }

}