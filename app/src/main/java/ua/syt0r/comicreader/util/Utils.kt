package ua.syt0r.comicreader.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.ParcelFileDescriptor
import androidx.core.content.ContextCompat
import com.shockwave.pdfium.PdfiumCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.syt0r.comicreader.ComicApplication
import ua.syt0r.comicreader.database.ComicDatabase
import ua.syt0r.comicreader.database.entity.DbFile
import java.io.File
import java.util.regex.Pattern

val IMAGE_EXTENSIONS = arrayOf("jpg", "png", "jpeg", "bmp")
val ZIP_EXTENSIONS = arrayOf("zip", "cbz")
val RAR_EXTENSIONS = arrayOf("rar", "cbr")

fun getComponent(activity: Activity) = (activity.application as ComicApplication).component

fun getFileType(file: File): Int {

    if (file.isFile) {

        val path = file.path
        val indexOfLastSlash = path.lastIndexOf("/")
        val nameWithExtension = path.substring(indexOfLastSlash + 1)
        val dotIndex = nameWithExtension.lastIndexOf(".")
        val extension = if (dotIndex == -1) "" else nameWithExtension.substring(Math.min(dotIndex + 1, nameWithExtension.length))

        when(extension) {
            in IMAGE_EXTENSIONS -> return FileType.IMAGE
            in ZIP_EXTENSIONS -> return FileType.ZIP
            in RAR_EXTENSIONS -> return FileType.RAR
            "pdf" -> return FileType.PDF
            else -> FileType.UNKNOWN
        }

    }

    return FileType.FOLDER

}

fun getImagesFromFolder(dir: File): ArrayList<String> {
    val files = ArrayList<String>()
    dir.listFiles().forEach {
        val p = it.path
        if (p.substring(p.lastIndexOf(".") + 1) in IMAGE_EXTENSIONS)
            files.add(it.path)
    }
    files.sortWith(Comparator { p1,p2 ->
        compareStringsWithNumbers(
            p1,
            p2
        )
    })
    return files
}

fun createThumbnail(context: Context, database: ComicDatabase, file: File) = CoroutineScope(Dispatchers.IO).launch {
    val fileType = getFileType(file)

    var thumbPath: String? = null

    when(fileType) {
        FileType.PDF -> {


            val pdfiumCore = PdfiumCore(context)
            val pdfDoc = pdfiumCore.newDocument(ParcelFileDescriptor.open(file,
                ParcelFileDescriptor.MODE_READ_ONLY))

            val width = 200
            val height = 200
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            pdfiumCore.renderPageBitmap(pdfDoc, bitmap, 0, 0, 0, width, height)

            val thumbFile = File(ContextCompat.getExternalFilesDirs(context, null).first(),
                "${System.currentTimeMillis()}.png")
            val fOut = thumbFile.outputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()

            thumbPath = thumbFile.path

        }
    }

    thumbPath?.also {
        val dao = database.dbFileDao()
        val dbFile = dao.getByFilePath(file.path)
        if (dbFile != null) {
            dbFile.thumb = it
            dao.update(dbFile)
        } else {
            dao.insert(DbFile(0L, file.path,  0L, fileType, 0, 0, it))
        }
    }

}

fun compareFilesWithNumbers(f1: File, f2: File): Int {

    if ((f1.isFile && f2.isFile) || (!f1.isFile && !f2.isFile))
        return compareStringsWithNumbers(f1.name, f2.name)

    if (f1.isFile)
        return 1

    return -1

}

fun compareStringsWithNumbers(s1: String, s2: String): Int{

    val s1Nums = getNumbersFromString(s1)
    val s2Nums = getNumbersFromString(s2)

    var i = 0
    while (i < s1Nums.size && i < s2Nums.size){
        if (s1Nums[i] > s2Nums[i]) return 1
        if (s1Nums[i] < s2Nums[i]) return (-1)
        i++
    }

    if (s1Nums.size > s2Nums.size)
        return 1
    if (s1Nums.size < s2Nums.size)
        return (-1)

    return if (s1 > s2) 1 else 0

}

private fun getNumbersFromString(string: String): ArrayList<Double>{
    val nums = ArrayList<Double>()
    Pattern.compile("-?\\d+").matcher(string).also { matcher ->
        while (matcher.find()) nums.add(matcher.group().toDouble())
    }
    return nums
}