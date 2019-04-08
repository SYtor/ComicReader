package ua.syt0r.comicreader.ui.fragment.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ua.syt0r.comicreader.db.ComicDatabase
import ua.syt0r.comicreader.db.entity.History

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ComicDatabase.getInstance(application)
    val historyRecords = database.historyDao().subscribeOnHistory()

    fun updateReadDate(history: History) {
        history.readTime = System.currentTimeMillis()
        database.historyDao().update(history)
    }
}