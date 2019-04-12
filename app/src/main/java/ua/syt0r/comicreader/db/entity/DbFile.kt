package ua.syt0r.comicreader.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("path", unique = true)])
data class DbFile(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var path: String = "",
    var readTime: Long = 0L,
    var type: Int = 0,
    var pinned: Int = 0,
    var pinnedDate: Long = 0L,
    var thumb: String = ""
)