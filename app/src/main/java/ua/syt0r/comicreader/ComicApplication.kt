package ua.syt0r.comicreader

import android.widget.Toast
import androidx.multidex.MultiDexApplication
import ua.syt0r.comicreader.dagger.*
import ua.syt0r.comicreader.database.entity.DbFile
import javax.inject.Inject

class ComicApplication : MultiDexApplication() {

    val component = DaggerApplicationComponent.create()


    @Inject lateinit var file: DbFile

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        Toast.makeText(this, "Test ${file.path}", Toast.LENGTH_LONG).show()
    }

}