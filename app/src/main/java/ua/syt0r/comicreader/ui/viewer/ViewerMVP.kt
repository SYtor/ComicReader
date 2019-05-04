package ua.syt0r.comicreader.ui.viewer

import ua.syt0r.comicreader.ui.base.BasePresenter

class ViewerMVP {

    interface View {
        fun setRenderer(rendererType: Int)
        fun setData(data: Any)
        fun showErrorMessage(message: String)
        fun getReadingPosition(): Int
        fun scrollToPosition(position: Int)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun loadFile(filePath: String?)
    }

}