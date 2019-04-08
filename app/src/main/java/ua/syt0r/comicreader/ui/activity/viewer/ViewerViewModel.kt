package ua.syt0r.comicreader.ui.activity.viewer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ua.syt0r.comicreader.db.ComicDatabase

class ViewerViewModel(application: Application) : AndroidViewModel(application) {
    val database = ComicDatabase.getInstance(application)
}