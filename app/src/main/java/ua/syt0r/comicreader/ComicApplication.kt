package ua.syt0r.comicreader

import androidx.multidex.MultiDexApplication
import ua.syt0r.comicreader.dagger.*
import ua.syt0r.comicreader.ui.browse.BrowseModule
import ua.syt0r.comicreader.ui.history.HistoryModule
import ua.syt0r.comicreader.ui.home.HomeModule
import ua.syt0r.comicreader.ui.viewer.ViewerModule

class ComicApplication : MultiDexApplication() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .databaseModule(DatabaseModule())
            .homeModule(HomeModule())
            .historyModule(HistoryModule())
            .browseModule(BrowseModule())
            .viewerModule(ViewerModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

}