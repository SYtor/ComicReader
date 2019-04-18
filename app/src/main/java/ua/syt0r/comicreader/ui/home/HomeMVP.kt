package ua.syt0r.comicreader.ui.home

import ua.syt0r.comicreader.database.entity.DbFile

class HomeMVP {

    interface View {

        fun showPins(pins: List<DbFile>)
        fun showNoPins()

        fun showHistory(history: List<DbFile>)
        fun showNoHistory()

    }

    interface Presenter {

        fun loadData()

    }

}