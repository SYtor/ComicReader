package ua.syt0r.comicreader.ui.browse

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.util.FileType
import ua.syt0r.comicreader.util.compareFilesWithNumbers
import ua.syt0r.comicreader.util.getFileType
import java.io.File
import javax.inject.Inject

class BrowsePresenter @Inject constructor(
    private val application: Application,
    private val database: ComicDatabase
) : BrowseMVP.Presenter() {

    private val disposable = CompositeDisposable()

    private var folderLevel = 0
    private var currentDir: File? = null
    private val currentFiles = ArrayList<File>()

    override fun updateList(dir: File?, isUpDirSelected: Boolean) {

        disposable.add(
            Completable.fromAction {

                currentFiles.clear()

                when {

                    dir == null -> {
                        folderLevel = 0
                        currentDir = null
                        currentFiles.addAll(loadStorageWithPins())
                    }

                    isUpDirSelected -> {
                        folderLevel -= 1
                        if (folderLevel == 0) {
                            currentFiles.addAll(loadStorageWithPins())
                        } else {
                            currentDir = dir
                            currentFiles.addAll(loadDirectory(dir))
                        }
                    }

                    else -> {
                        folderLevel += 1
                        currentDir = dir
                        currentFiles.addAll(loadDirectory(dir))
                    }

                }

            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.setProgressBarLoading(true) }
                .subscribe {
                    view?.updateList(folderLevel == 0, currentFiles)
                    view?.setProgressBarLoading(false)
                }
        )

    }

    override fun pinFile(file: File) {
        disposable.add(
            Completable.fromAction {
                val dbFile = database.dbFileDao().getByFilePath(file.path)
                if (dbFile != null) {
                    dbFile.pinned = 1
                    dbFile.pinnedDate = System.currentTimeMillis()
                    database.dbFileDao().update(dbFile)
                } else {
                    database.dbFileDao().insert(
                        DbFile(0, file.path, 0, getFileType(file),
                            1, System.currentTimeMillis(), 0, "")
                    )
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&  ContextCompat.checkSelfPermission(application,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            view?.provideFragment()?.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)

        }
    }

    override fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {

                view?.onPermissionUpdate((grantResults.isNotEmpty() &&
                        grantResults.first() == PackageManager.PERMISSION_GRANTED))

            }
        }
    }

    override fun onViewDestroyed() {
        super.onViewDestroyed()
        disposable.dispose()
    }

    private fun loadStorageWithPins(): List<File> {
        val files = ArrayList<File>()

        //Storage list
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(application, null)
        externalFilesDirs.forEach {
            val path = it.path
            files.add(File(path.substring(0, path.indexOf("/Android/data/"))))
        }

        //Pins
        val pins = database.dbFileDao().observePins().blockingFirst()
        pins.forEach { if (it.type == FileType.FOLDER) files.add(File(it.path)) }

        return files
    }

    private fun loadDirectory(dir: File): List<File> {
        val files = ArrayList<File>()

        val upDir = File(dir.parent)
        currentFiles.add(upDir)

        dir.listFiles()?.asList()
            ?.sortedWith(Comparator { f1, f2 -> compareFilesWithNumbers(f1, f2) })
            ?.also { contents -> currentFiles.addAll(contents) }

        return files
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 969
    }

}