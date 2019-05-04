package ua.syt0r.comicreader.dagger

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ua.syt0r.comicreader.database.ComicDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): ComicDatabase {
        return Room.databaseBuilder(application, ComicDatabase::class.java, ComicDatabase.DATABASE_NAME).build()
    }

}