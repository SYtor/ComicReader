package ua.syt0r.comicreader.ui.home

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.comicreader.database.ComicDatabase
import javax.inject.Inject

class HomePresenter @Inject constructor(var database: ComicDatabase) : HomeMVP.Presenter() {

    private val disposable = CompositeDisposable()

    override fun loadData() {
        disposable.addAll(

            database.dbFileDao().observeHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {  }
                .subscribe {
                    if (it.isEmpty())
                        view?.showNoHistory()
                    else
                        view?.showHistory(it)
                },

            database.dbFileDao().observePins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {  }
                .subscribe {
                    if (it.isEmpty())
                        view?.showNoPins()
                    else
                        view?.showPins(it)
                }
        )

    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        disposable.dispose()
    }

}