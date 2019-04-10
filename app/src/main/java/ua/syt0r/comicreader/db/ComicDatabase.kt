package ua.syt0r.comicreader.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DbFile::class], version = 1)
abstract class ComicDatabase : RoomDatabase() {

    abstract fun dbFileDao(): DbFileDao

    companion object {

        private const val DATABASE_NAME = "database"
        private var instance: ComicDatabase? = null

        fun getInstance(context: Context): ComicDatabase {
            synchronized(this){
                instance?.also { return it }
                val inst = Room.databaseBuilder(context.applicationContext, ComicDatabase::class.java,
                    ComicDatabase.DATABASE_NAME) .build()
                instance = inst
                return inst

            }
        }
    }

}