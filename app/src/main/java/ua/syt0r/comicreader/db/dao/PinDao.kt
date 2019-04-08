package ua.syt0r.comicreader.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ua.syt0r.comicreader.db.entity.Pin

@Dao
interface PinDao {

    @Query("SELECT * FROM Pin ORDER BY pinnedDate DESC")
    fun subscribeOnPins(): LiveData<List<Pin>>

    @Update
    fun update(pin: Pin)

    @Delete
    fun delete(pin: Pin)

    @Insert
    fun insert(pin: Pin)

}