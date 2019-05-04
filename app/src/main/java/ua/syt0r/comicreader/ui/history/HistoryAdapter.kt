package ua.syt0r.comicreader.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ua.syt0r.comicreader.util.FileType
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.util.OnDBFileClickListener
import java.io.File

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var historyRecords: List<DbFile>? = null
    var onDBFileClickListener: OnDBFileClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_history_item, parent, false))
    }

    override fun getItemCount(): Int {
        return historyRecords?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        historyRecords?.getOrNull(position)?.also { history ->
            val fileName = File(history.path).name
            holder.textView.text = fileName
            when (history.type) {
                FileType.IMAGE -> Picasso.get().load(history.path).into(holder.imageView)
                FileType.PDF -> holder.imageView.setImageResource(R.drawable.ic_google_drive_pdf_file)
                FileType.FOLDER -> holder.imageView.setImageResource(R.drawable.ic_folder)
                else -> holder.imageView.setImageResource(R.drawable.ic_file)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.image)
        val textView: TextView = itemView.findViewById(R.id.text)

        init {
            itemView.setOnClickListener {
                historyRecords?.getOrNull(adapterPosition)?.also { onDBFileClickListener?.onClick(it) }
            }
        }

    }

}