package ua.syt0r.comicreader.ui.fragment.browse

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ua.syt0r.comicreader.db.ComicDatabase

class BrowseViewModel(application: Application) : AndroidViewModel(application) {

    val database = ComicDatabase.getInstance(application)

    fun getDbFile(path: String) = database.dbFileDao().getByFilePath(path)

}