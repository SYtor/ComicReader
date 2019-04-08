package ua.syt0r.comicreader.ui.activity.viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import me.zhanghai.android.systemuihelper.SystemUiHelper
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.ui.activity.viewer.renderer.pdf.PdfRenderer
import ua.syt0r.comicreader.ui.activity.viewer.renderer.image.ImageRenderer
import ua.syt0r.comicreader.ui.activity.viewer.renderer.Renderer
import ua.syt0r.comicreader.ui.activity.viewer.renderer.RendererContainer

class ViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val rendererContainer = findViewById<RendererContainer>(R.id.renderer_container)
        val bottomLayout = findViewById<View>(R.id.bottom_panel)
        val seekBar = findViewById<SeekBar>(R.id.chapter_progress)
        val progressText = findViewById<TextView>(R.id.chapter_progress_text)

        //Setup toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val systemUiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE,
            SystemUiHelper.FLAG_IMMERSIVE_STICKY, SystemUIVisibilityListener(toolbar, bottomLayout))
        systemUiHelper.show()

        //Setup recycler

        val type = intent.getIntExtra(TYPE_KEY, TYPE_IMAGE_ARRAY)

        val renderer = when (type) {
            TYPE_IMAGE_ARRAY -> ImageRenderer(this, intent)
            TYPE_PDF -> PdfRenderer(this, intent)
            else -> ImageRenderer(this, intent)
        }

        rendererContainer.setOnClickListener { systemUiHelper.toggle() }

        renderer.positionChangeListener = object : Renderer.PositionChangeListener {
            override fun onPositionChanged(position: Int, total: Int) {
                seekBar.max = total - 1
                seekBar.progress = position
            }
        }

        val rendererView = renderer.onCreateView()
        rendererContainer.addView(rendererView)

        //Setup seek bar

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var isTouching = false
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressText.text = "${seekBar?.progress?.plus(1) ?: 0}/${seekBar?.max?.plus(1) ?: 0}"
                if (isTouching)
                    renderer.scrollToPosition(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {isTouching = true }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { isTouching = false }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.viewer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.options -> {
                OptionsFragment().show(supportFragmentManager,  null)
            }
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val TYPE_KEY = "type_key"

        const val TYPE_IMAGE_ARRAY = 0
        const val TYPE_PDF = 1

        const val IMAGE_ARRAY_KEY = "array_key"
        const val FILE_KEY = "file_key"

        const val DEFAULT_POSITION_KEY = "position_key"
    }

}
