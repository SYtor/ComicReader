package ua.syt0r.comicreader.ui.viewer

import android.app.Application
import dagger.Module
import dagger.Provides
import ua.syt0r.comicreader.database.ComicDatabase
import javax.inject.Inject

@Module
class ViewerModule {

    @Provides
    fun providePresenter(application: Application, database: ComicDatabase) : ViewerMVP.Presenter {
        return ViewerPresenter(application, database)
    }

}