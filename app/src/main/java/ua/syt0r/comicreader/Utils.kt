package ua.syt0r.comicreader

import ua.syt0r.comicreader.ui.fragment.browse.BrowserFragment
import java.io.File
import java.util.regex.Pattern

class Utils {

    companion object {

        val IMAGE_EXTENSIONS = arrayOf("jpg", "png", "jpeg", "bmp")
        val ZIP_EXTENSIONS = arrayOf("zip", "cbz")
        val RAR_EXTENSIONS = arrayOf("rar", "cbr")

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
            files.sortWith(Comparator { p1,p2 -> Utils.compareStringsWithNumbers(p1, p2) })
            return files
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

    }


}