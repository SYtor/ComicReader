package ua.syt0r.comicreader.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ua.syt0r.comicreader.database.ComicDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val database = ComicDatabase.getInstance(application)
    val mutableHistory = database.dbFileDao().subscribeOnHistory()
    val mutablePins = database.dbFileDao().subscribeOnPins()

}