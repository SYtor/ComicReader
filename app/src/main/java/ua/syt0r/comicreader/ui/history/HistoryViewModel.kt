package ua.syt0r.comicreader.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.database.entity.DbFile

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ComicDatabase.getInstance(application)
    val historyRecords = database.dbFileDao().subscribeOnHistory()

    fun updateReadDate(dbFile: DbFile) {
        dbFile.readTime = System.currentTimeMillis()
        database.dbFileDao().update(dbFile)
    }

}