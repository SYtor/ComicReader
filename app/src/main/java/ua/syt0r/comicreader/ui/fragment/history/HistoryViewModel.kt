package ua.syt0r.comicreader.ui.fragment.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ua.syt0r.comicreader.db.ComicDatabase
import ua.syt0r.comicreader.db.DbFile

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ComicDatabase.getInstance(application)
    val historyRecords = database.dbFileDao().subscribeOnHistory()

    fun updateReadDate(dbFile: DbFile) {
        dbFile.readTime = System.currentTimeMillis()
        database.dbFileDao().update(dbFile)
    }

}