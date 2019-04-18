package ua.syt0r.comicreader.ui.history

import ua.syt0r.comicreader.database.entity.DbFile

class HistoryMVP {

    interface View {



    }

    interface Presenter {

        fun loadHistory()
        fun updateReadingDate(dbFile: DbFile)

    }

}