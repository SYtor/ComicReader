package ua.syt0r.comicreader.ui.activity.viewer.renderer

import android.content.Context
import android.content.Intent
import android.view.View

open class Renderer(context: Context, intent: Intent) {

    var positionChangeListener: PositionChangeListener? = null

    open fun scrollToPosition(position: Int) {}
    open fun onCreateView(): View { return View(null) }

    interface PositionChangeListener {
        fun onPositionChanged(position: Int, total: Int)
    }

}