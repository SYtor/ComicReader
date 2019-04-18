package ua.syt0r.comicreader.ui.history

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.ui.base.BasePresenter
import javax.inject.Inject

class HistoryPresenter @Inject constructor(val database: ComicDatabase) :
    BasePresenter<HistoryMVP.View>(), HistoryMVP.Presenter {

    override fun loadHistory() {

    }

    override fun updateReadingDate(dbFile: DbFile) {
        Completable.fromAction {
            database.dbFileDao().update(dbFile)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            }
    }

}