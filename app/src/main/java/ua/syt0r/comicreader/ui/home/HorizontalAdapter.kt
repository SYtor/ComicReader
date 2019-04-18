package ua.syt0r.comicreader.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.util.FileType
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.database.entity.DbFile
import java.io.File

class HorizontalAdapter : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>(){

    var list: List<DbFile>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_horizontal_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.getOrNull(position)?.also { dbFile ->
            holder.textView.text = File(dbFile.path).name
            when(dbFile.type) {
                FileType.FOLDER -> holder.imageView.setImageResource(R.drawable.ic_folder)
                FileType.IMAGE -> {}
                FileType.PDF -> holder.imageView.setImageResource(R.drawable.ic_google_drive_pdf_file)
                else -> holder.imageView.setImageResource(R.drawable.ic_file)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val textView: TextView = itemView.findViewById(R.id.text)
    }

}