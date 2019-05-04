package ua.syt0r.comicreader.ui.home

import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.ui.base.BasePresenter

class HomeMVP {

    interface View {

        fun showPins(pins: List<DbFile>)
        fun showNoPins()

        fun showHistory(history: List<DbFile>)
        fun showNoHistory()

    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun loadData()
    }

}