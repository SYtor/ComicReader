package ua.syt0r.comicreader.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ua.syt0r.comicreader.db.entity.DbFile

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(file: DbFile): Long

    @Query("SELECT * FROM DbFile WHERE path=:path")
    fun getByFilePath(path: String): DbFile?

    @Query("SELECT thumb FROM DbFile WHERE path=:path")
    fun getThumb(path: String): String?

}