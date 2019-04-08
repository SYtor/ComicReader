package ua.syt0r.comicreader.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pin(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var path: String = "",
    var type: Int = 0,
    var pinnedDate: Long = 0L
)

class PinType {
    companion object {
        const val IMAGE = 0
        const val FILE = 1
        const val FOLDER = 2
    }
}