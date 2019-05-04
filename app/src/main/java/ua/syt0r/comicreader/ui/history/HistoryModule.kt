package ua.syt0r.comicreader.ui.history

import dagger.Module
import dagger.Provides
import ua.syt0r.comicreader.database.ComicDatabase

@Module
class HistoryModule {

    @Provides
    fun provideHistoryPresenter(database: ComicDatabase): HistoryMVP.Presenter = HistoryPresenter(database)

}