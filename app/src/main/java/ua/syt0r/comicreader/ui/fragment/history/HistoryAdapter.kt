package ua.syt0r.comicreader.ui.fragment.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.db.entity.DbFile
import java.io.File

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var historyRecords: List<DbFile>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_horizontal_item, parent, false))
    }

    override fun getItemCount(): Int {
        return historyRecords?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        historyRecords?.getOrNull(position)?.also {  history ->
            val fileName = File(history.path).name
            holder.textView.text = fileName
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val textView = itemView.findViewById<TextView>(R.id.text)
    }

}