package ua.syt0r.comicreader.ui.activity.viewer

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hzy.libp7zip.P7ZipApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.syt0r.comicreader.FileType
import ua.syt0r.comicreader.Utils
import ua.syt0r.comicreader.db.ComicDatabase
import ua.syt0r.comicreader.db.entity.DbFile
import java.io.File

class ViewerViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ComicDatabase.getInstance(application)
    private val tmpFolder: File
    private val thumbFolder: File

    val mutableData = MutableLiveData<Any>()

    init {
        val root = ContextCompat.getExternalFilesDirs(application, null).first()
        tmpFolder = File(root, "tmp")
        tmpFolder.mkdir()
        thumbFolder = File("thumb")
        thumbFolder.mkdir()
    }

    fun loadData(file: File, type: Int) = CoroutineScope(Dispatchers.IO).launch {
        mutableData.postValue(null)

        when(type) {
            FileType.IMAGE -> loadImages(file)
            FileType.ZIP -> loadZip(file)
            FileType.RAR -> loadRar(file)
            FileType.PDF -> loadPdf(file)
        }

        //TODO update if exist

        val dbFile = database.dbFileDao().getByFilePath(file.path)
        if (dbFile == null) {
            val newDbFile = DbFile()
            newDbFile.path = file.path
            newDbFile.readTime = System.currentTimeMillis()
            newDbFile.type = type
            database.dbFileDao().insert(newDbFile)
        } else {
            dbFile.readTime = System.currentTimeMillis()
            database.dbFileDao().update(dbFile)
        }

    }

    override fun onCleared() {
        CoroutineScope(Dispatchers.IO).launch { tmpFolder.deleteRecursively() }
    }

    private fun loadImages(file: File) {
        val files = Utils.getImagesFromFolder(file.parentFile)
        mutableData.postValue(files)
    }

    private fun loadZip(file: File) {}

    private fun loadRar(file: File) {

        val command = "7z x '$file' '-o$tmpFolder' -aoa"
        val result = P7ZipApi.executeCommand(command)
        Log.d("Debug", "Command: $command")
        Log.d("Debug", "Command result: $result")

        val images = Utils.getImagesFromFolder(tmpFolder)
        mutableData.postValue(images)

    }

    private fun loadPdf(file: File) {
        mutableData.postValue(file.absolutePath)
    }

}