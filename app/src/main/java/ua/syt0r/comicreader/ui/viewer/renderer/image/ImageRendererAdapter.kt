package ua.syt0r.comicreader.ui.viewer.renderer.image

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.piasy.biv.view.BigImageView
import ua.syt0r.comicreader.R

class ImageRendererAdapter : RecyclerView.Adapter<ImageRendererAdapter.ViewHolder>() {

    var files: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_viewer_item, parent, false))
    }

    override fun getItemCount(): Int {
        return files?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        files?.getOrNull(position)?.also { path ->
            holder.image.showImage(Uri.parse(path))
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: BigImageView = itemView.findViewById(R.id.big_image_view)
    }

}