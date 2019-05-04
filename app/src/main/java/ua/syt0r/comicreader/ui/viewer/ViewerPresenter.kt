package ua.syt0r.comicreader.ui.viewer

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import com.hzy.libp7zip.P7ZipApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.ui.viewer.renderer.Renderer
import ua.syt0r.comicreader.util.FileType
import ua.syt0r.comicreader.util.getFileType
import ua.syt0r.comicreader.util.getImagesFromFolder
import java.io.File
import java.lang.IllegalStateException
import javax.inject.Inject

class ViewerPresenter @Inject constructor(
    application: Application,
    val database: ComicDatabase
) : ViewerMVP.Presenter() {

    private val tmpFolder: File

    init {
        val root = ContextCompat.getExternalFilesDirs(application, null).first()
        tmpFolder = File(root, "tmp")
        tmpFolder.mkdir()
    }

    private val disposable = CompositeDisposable()

    override fun loadFile(filePath: String?) {

        disposable.add(
            observeFileType(filePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { type ->

                        when(type) {
                            FileType.IMAGE, FileType.RAR, FileType.ZIP, FileType.FOLDER ->
                                view?.setRenderer(Renderer.IMAGE)
                            FileType.PDF -> view?.setRenderer(Renderer.PDF)
                            else -> throw IllegalStateException("Unknown file type")
                        }

                        updateHistory(filePath!!, type)
                        loadData(type, File(filePath))

                    },
                    { view?.showErrorMessage(it.message ?: "") }
                )
        )
    }

    private fun observeFileType(path: String?) : Observable<Int> = Observable.fromCallable {
        if (path == null) throw IllegalStateException("Unknown file path")
        getFileType(File(path))
    }

    private fun updateHistory(path: String, fileType: Int) {

        disposable.add(
            Observable.create<Void> {
                val path2 = if (fileType == FileType.IMAGE) File(path).parent else path

                val dbFile = database.dbFileDao().getByFilePath(path2)
                if (dbFile == null) {
                    val newFile = DbFile(0, path2, System.currentTimeMillis(), fileType, 0, 0, 0, "")
                    database.dbFileDao().insert(newFile)
                } else {
                    dbFile.readTime = System.currentTimeMillis()
                    database.dbFileDao().update(dbFile)
                }
                return@create
            }.subscribeOn(Schedulers.io()).subscribe()
        )



    }

    private fun loadData(fileType: Int, file: File) {
        disposable.add(
            Observable.fromCallable {

                //TODO load files list with same format for next/previous

                return@fromCallable when(fileType) {
                    FileType.IMAGE -> getImagesFromFolder(file.parentFile)
                    FileType.FOLDER -> getImagesFromFolder(file)
                    FileType.ZIP -> loadZip(file)
                    FileType.RAR -> loadRar(file)
                    FileType.PDF -> file.absolutePath
                    else -> throw IllegalStateException("Unknown error")
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        view?.setData(it)
                        if (fileType == FileType.IMAGE && it is List<*>)
                            view?.scrollToPosition(it.indexOf(file.absolutePath))
                    },
                    { view?.showErrorMessage(it.message ?: "")}
                )
        )
    }

    private fun loadZip(file: File) {}

    private fun loadRar(file: File) : List<String> {

        val command = "7z x '$file' '-o$tmpFolder' -aoa"
        val result = P7ZipApi.executeCommand(command)
        Log.d("Debug", "Command: $command")
        Log.d("Debug", "Command result: $result")

        return getImagesFromFolder(tmpFolder)

    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        disposable.dispose()
        clearTmp()
    }

    private fun clearTmp() {
        CoroutineScope(Dispatchers.IO).launch { tmpFolder.deleteRecursively() }
    }

}