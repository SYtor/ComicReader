package ua.syt0r.comicreader

import java.io.File
import java.util.regex.Pattern

class Utils {

    companion object {

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