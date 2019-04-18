package ua.syt0r.comicreader.ui.viewer.renderer

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat

class RendererContainer : FrameLayout {

    private lateinit var gestureDetectorCompat: GestureDetectorCompat

    constructor(context: Context): super(context){
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int):
            super(context, attributeSet, defStyle){
        init(context)
    }

    private fun init(context: Context){
        gestureDetectorCompat = GestureDetectorCompat(context, ClickListener())
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        gestureDetectorCompat.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private inner class ClickListener : GestureDetector.SimpleOnGestureListener(){

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            performClick()
            return true
        }

    }

}