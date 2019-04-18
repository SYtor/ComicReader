package ua.syt0r.comicreader.dagger

import dagger.Component
import ua.syt0r.comicreader.ComicApplication
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.ui.main.MainActivity
import javax.inject.Singleton

@Component(modules = [TestModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(application: ComicApplication)
    fun inject(activity: MainActivity)
    fun dbFile() : DbFile
}