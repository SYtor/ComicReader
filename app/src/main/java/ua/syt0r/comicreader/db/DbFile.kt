package ua.syt0r.comicreader.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbFile(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var path: String = "",
    var readTime: Long = 0L,
    var type: Int = 0,
    var pinned: Int = 0,
    var pinnedDate: Long = 0L
)