package ua.syt0r.comicreader.ui.viewer.renderer

import android.content.Context
import android.view.View

open class Renderer(context: Context) {

    var positionChangeListener: PositionChangeListener? = null

    open fun scrollToPosition(position: Int) {}
    open fun onCreateView(): View { return View(null) }
    open fun setData(data: Any) {}

    interface PositionChangeListener {
        fun onPositionChanged(position: Int, total: Int)
    }

}