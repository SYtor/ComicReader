package ua.syt0r.comicreader.dagger

import android.app.Application
import dagger.Component
import ua.syt0r.comicreader.ComicApplication
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.ui.home.HomeFragment
import ua.syt0r.comicreader.ui.home.HomeModule
import ua.syt0r.comicreader.ui.home.HomePresenter
import ua.syt0r.comicreader.ui.main.MainActivity
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class, DatabaseModule::class, HomeModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(application: ComicApplication)
    fun inject(activity: MainActivity)

    fun inject(fragment: HomeFragment)

    fun getApplication(): Application
    fun getDatabase(): ComicDatabase

    fun getHomePresenter(): HomePresenter

}