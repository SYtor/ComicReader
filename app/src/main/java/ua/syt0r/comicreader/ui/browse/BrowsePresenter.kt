package ua.syt0r.comicreader.ui.browse

import ua.syt0r.comicreader.database.ComicDatabase
import javax.inject.Inject

class BrowsePresenter {
    @Inject lateinit var database: ComicDatabase
}