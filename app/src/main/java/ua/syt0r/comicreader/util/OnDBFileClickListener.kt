package ua.syt0r.comicreader.util

import ua.syt0r.comicreader.database.entity.DbFile

interface OnDBFileClickListener {
    fun onClick(dbFile: DbFile)
}