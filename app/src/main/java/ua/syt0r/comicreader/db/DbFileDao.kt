package ua.syt0r.comicreader.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DbFileDao {

    @Query("SELECT * FROM DbFile WHERE readTime > 0 ORDER BY readTime DESC")
    fun subscribeOnHistory(): LiveData<List<DbFile>>

    @Query("SELECT * FROM DbFile WHERE pinned=1 ORDER BY pinnedDate DESC")
    fun subscribeOnPins(): LiveData<List<DbFile>>

    @Update
    fun update(file: DbFile)

    @Delete
    fun delete(file: DbFile)

    @Insert
    fun insert(file: DbFile): Long

}