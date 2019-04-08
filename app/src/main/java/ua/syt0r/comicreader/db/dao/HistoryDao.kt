package ua.syt0r.comicreader.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ua.syt0r.comicreader.db.entity.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM History ORDER BY readTime DESC")
    fun subscribeOnHistory(): LiveData<List<History>>

    @Update
    fun update(history: History)

    @Delete
    fun delete(history: History)

    @Insert
    fun insert(history: History)

}