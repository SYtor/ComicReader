package ua.syt0r.comicreader.ui.home

import dagger.Module
import dagger.Provides
import ua.syt0r.comicreader.database.ComicDatabase

@Module
class HomeModule {

    @Provides
    fun provideHomePresenter(database: ComicDatabase): HomeMVP.Presenter = HomePresenter(database)

}