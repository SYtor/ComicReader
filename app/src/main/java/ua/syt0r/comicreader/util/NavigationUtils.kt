package ua.syt0r.comicreader.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.ui.viewer.ViewerActivity
import java.io.File

class NavigationUtils {
    companion object {

        fun navigate(file: File, navController: NavController, context: Context) {

            when(getFileType(file)) {

                FileType.PDF, FileType.ZIP, FileType.RAR, FileType.IMAGE -> {
                    context.startActivity(Intent(context, ViewerActivity::class.java).apply {
                        data = Uri.fromFile(file)
                    })
                }

                FileType.FOLDER -> {
                    navController.navigate(R.id.browse_fragment, Bundle().apply {
                        putString("","")
                    })
                }

            }
        }
    }

}