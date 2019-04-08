package ua.syt0r.comicreader.ui.activity.viewer.renderer.pdf

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.github.barteksc.pdfviewer.PDFView
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.ui.activity.viewer.ViewerActivity
import ua.syt0r.comicreader.ui.activity.viewer.renderer.Renderer
import java.io.File

class PdfRenderer(private val context: Context, private val intent: Intent) : Renderer(context, intent) {

    private lateinit var pdfView: PDFView

    override fun onCreateView(): View {

        val root = LayoutInflater.from(context).inflate(R.layout.renderer_pdf_layout, null)
        pdfView = root.findViewById(R.id.pdf_view)

        val path = intent.getStringExtra(ViewerActivity.FILE_KEY)
        pdfView.fromFile(File(path))
            .onPageChange { page, pageCount -> positionChangeListener?.onPositionChanged(page, pageCount) }
            .onLoad { positionChangeListener?.onPositionChanged(0, it) }
            .swipeHorizontal(true)
            .pageSnap(true)
            .autoSpacing(true)
            .pageFling(true)
            .load()

        return root
    }

    override fun scrollToPosition(position: Int) {
        pdfView.jumpTo(position)
    }

}
