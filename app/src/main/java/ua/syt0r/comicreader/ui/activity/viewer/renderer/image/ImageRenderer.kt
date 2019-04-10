package ua.syt0r.comicreader.ui.activity.viewer.renderer.image

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.ui.activity.viewer.renderer.Renderer

class ImageRenderer(private val context: Context) : Renderer(context) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageRendererAdapter

    override fun onCreateView(): View {

        val root = LayoutInflater.from(context).inflate(R.layout.renderer_image_layout, null)
        recyclerView = root.findViewById(R.id.recycler)

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = ImageRendererAdapter()
        recyclerView.adapter = adapter

        BigImageViewer.initialize(FrescoImageLoader.with(context))

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE)
                    positionChangeListener?.onPositionChanged(layoutManager.findFirstVisibleItemPosition(),
                        adapter.files?.size ?: 0)
            }
        })

        return root
    }

    override fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

    override fun setData(data: Any) {
        val list = data as List<String>
        adapter.files = list
        adapter.notifyDataSetChanged()
        positionChangeListener?.onPositionChanged(0, list.size)
    }

}