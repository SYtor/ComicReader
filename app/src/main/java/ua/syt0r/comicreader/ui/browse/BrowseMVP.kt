package ua.syt0r.comicreader.ui.browse

import androidx.fragment.app.Fragment
import ua.syt0r.comicreader.ui.base.BasePresenter
import java.io.File

class BrowseMVP {

    interface View {
        fun setProgressBarLoading(isLoading: Boolean)
        fun updateList(isTopLevelDir: Boolean, files: List<File>)
        fun provideFragment(): Fragment
        fun onPermissionUpdate(isGranted: Boolean)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun updateList(dir: File?, isUpDirSelected: Boolean)
        abstract fun pinFile(file: File)
        abstract fun checkPermission()
        abstract fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }

}