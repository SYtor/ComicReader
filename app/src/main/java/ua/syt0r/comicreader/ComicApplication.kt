package ua.syt0r.comicreader

import androidx.multidex.MultiDexApplication
import ua.syt0r.comicreader.dagger.*
import ua.syt0r.comicreader.ui.home.HomeModule

class ComicApplication : MultiDexApplication() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .databaseModule(DatabaseModule())
            .homeModule(HomeModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

}