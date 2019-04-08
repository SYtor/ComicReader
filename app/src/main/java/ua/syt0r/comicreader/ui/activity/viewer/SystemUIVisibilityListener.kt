package ua.syt0r.comicreader.ui.activity.viewer

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import me.zhanghai.android.systemuihelper.SystemUiHelper

class SystemUIVisibilityListener(private val toolbar: Toolbar, private val bottomLayout: View) :
    SystemUiHelper.OnVisibilityChangeListener{

    private val duration = 500L

    override fun onVisibilityChange(visible: Boolean) {
        if (visible) {
            toolbar.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(duration)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
            bottomLayout.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(duration)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
        } else {
            toolbar.animate()
                .alpha(0f)
                .translationY(-toolbar.bottom.toFloat())
                .setDuration(duration)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
            bottomLayout.animate()
                .alpha(0f)
                .translationY(bottomLayout.top.toFloat())
                .setDuration(duration)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
        }
    }
}