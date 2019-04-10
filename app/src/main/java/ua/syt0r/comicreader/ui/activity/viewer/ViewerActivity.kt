package ua.syt0r.comicreader.ui.activity.viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.zhanghai.android.systemuihelper.SystemUiHelper
import ua.syt0r.comicreader.FileType
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.Utils
import ua.syt0r.comicreader.ui.activity.viewer.renderer.pdf.PdfRenderer
import ua.syt0r.comicreader.ui.activity.viewer.renderer.image.ImageRenderer
import ua.syt0r.comicreader.ui.activity.viewer.renderer.Renderer
import ua.syt0r.comicreader.ui.activity.viewer.renderer.RendererContainer
import java.io.File

class ViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        val viewModel = ViewModelProviders.of(this).get(ViewerViewModel::class.java)

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

        //Load data

        val path = intent.data!!.path
        val file = File(path)
        val type = Utils.getFileType(file)
        viewModel.loadData(file, type)

        Log.d("Debug", "path: $path")
        Log.d("Debug", "type: $type")

        val renderer = when (type) {
            FileType.PDF -> PdfRenderer(this)
            else -> ImageRenderer(this)
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

        viewModel.mutableData.observe(this, Observer {

            if (it != null) {
                renderer.setData(it)
                return@Observer
            }

            //TODO loading message

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

}
