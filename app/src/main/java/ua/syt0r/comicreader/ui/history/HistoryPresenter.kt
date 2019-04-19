package ua.syt0r.comicreader.ui.history

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.comicreader.database.ComicDatabase
import javax.inject.Inject

class HistoryPresenter @Inject constructor(val database: ComicDatabase) : HistoryMVP.Presenter() {

    private val disposable = CompositeDisposable()

    override fun loadHistory() {
        disposable.add(
            database.dbFileDao().observeHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    if (it.isEmpty())
                        view?.showEmptyHistory()
                    else
                        view?.showHistory(it)
                }
        )
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        disposable.dispose()
    }

}