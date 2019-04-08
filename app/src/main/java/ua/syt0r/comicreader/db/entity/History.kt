package ua.syt0r.comicreader.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    var path: String = "",
    var readTime: Long = 0L
)