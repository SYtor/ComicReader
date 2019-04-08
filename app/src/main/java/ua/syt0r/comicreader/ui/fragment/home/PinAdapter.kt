package ua.syt0r.comicreader.ui.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.db.entity.Pin
import ua.syt0r.comicreader.db.entity.PinType
import java.io.File

class PinAdapter : RecyclerView.Adapter<PinAdapter.ViewHolder>(){

    var pins: List<Pin>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_horizontal_item, parent, false))
    }

    override fun getItemCount(): Int {
        return pins?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        pins?.getOrNull(position)?.also { pin ->
            when(pin.type) {
                PinType.IMAGE -> holder.imageView.setImageResource(R.drawable.ic_file)
                PinType.FILE -> holder.imageView.setImageResource(R.drawable.ic_file)
                PinType.FOLDER -> holder.imageView.setImageResource(R.drawable.ic_folder)
            }
            holder.textView.text = File(pin.path).name
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val textView = itemView.findViewById<TextView>(R.id.text)
    }

}