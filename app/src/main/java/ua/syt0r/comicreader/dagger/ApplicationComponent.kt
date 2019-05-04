package ua.syt0r.comicreader.dagger

import android.app.Application
import dagger.Component
import ua.syt0r.comicreader.ComicApplication
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.ui.browse.BrowseMVP
import ua.syt0r.comicreader.ui.browse.BrowseModule
import ua.syt0r.comicreader.ui.browse.BrowsePresenter
import ua.syt0r.comicreader.ui.browse.BrowserFragment
import ua.syt0r.comicreader.ui.history.HistoryFragment
import ua.syt0r.comicreader.ui.history.HistoryMVP
import ua.syt0r.comicreader.ui.history.HistoryModule
import ua.syt0r.comicreader.ui.history.HistoryPresenter
import ua.syt0r.comicreader.ui.home.HomeFragment
import ua.syt0r.comicreader.ui.home.HomeMVP
import ua.syt0r.comicreader.ui.home.HomeModule
import ua.syt0r.comicreader.ui.home.HomePresenter
import ua.syt0r.comicreader.ui.main.MainActivity
import ua.syt0r.comicreader.ui.viewer.ViewerActivity
import ua.syt0r.comicreader.ui.viewer.ViewerMVP
import ua.syt0r.comicreader.ui.viewer.ViewerModule
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, DatabaseModule::class, HomeModule::class, HistoryModule::class,
    BrowseModule::class, ViewerModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(application: ComicApplication)
    fun inject(activity: MainActivity)
    fun inject(activity: ViewerActivity)

    fun inject(fragment: HomeFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: BrowserFragment)

    fun getApplication(): Application
    fun getDatabase(): ComicDatabase

    fun getHomePresenter(): HomeMVP.Presenter
    fun getHistoryPresenter(): HistoryMVP.Presenter
    fun getBrowsePresenter(): BrowseMVP.Presenter

    fun getViewerPresenter(): ViewerMVP.Presenter

}