package ua.syt0r.comicreader.ui.viewer.renderer

import android.content.Context
import android.view.View

abstract class Renderer(context: Context) {

    var positionChangeListener: PositionChangeListener? = null

    abstract fun scrollToPosition(position: Int)
    abstract fun onCreateView(): View
    abstract fun setData(data: Any)
    abstract fun getReadingPosition(): Int

    interface PositionChangeListener {
        fun onPositionChanged(position: Int, total: Int)
    }

    companion object {
        const val IMAGE = 0
        const val PDF = 1
    }

}