package ua.syt0r.comicreader.ui.history

import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.ui.base.BasePresenter

class HistoryMVP {

    interface View {
        fun showHistory(history: List<DbFile>)
        fun showEmptyHistory()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun loadHistory()
    }

}