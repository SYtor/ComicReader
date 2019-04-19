package ua.syt0r.comicreader.ui.browse

import android.app.Application
import dagger.Module
import dagger.Provides
import ua.syt0r.comicreader.database.ComicDatabase

@Module
class BrowseModule {

    @Provides
    fun provideBrowsePresenter(application: Application, database: ComicDatabase): BrowseMVP.Presenter =
        BrowsePresenter(application, database)

}