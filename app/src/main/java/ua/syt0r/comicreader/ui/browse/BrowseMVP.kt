package ua.syt0r.comicreader.ui.browse

import ua.syt0r.comicreader.ui.base.BasePresenter
import java.io.File

class BrowseMVP {

    interface View {
        fun setProgressBarLoading(isLoading: Boolean)
        fun updateList(isTopLevelDir: Boolean, files: List<File>)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun updateList(dir: File?, isUpDirSelected: Boolean)
        abstract fun pinFile(file: File)
    }

}