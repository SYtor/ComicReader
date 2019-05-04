package ua.syt0r.comicreader.ui.viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import me.zhanghai.android.systemuihelper.SystemUiHelper
import ua.syt0r.comicreader.util.FileType
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.ui.viewer.renderer.pdf.PdfRenderer
import ua.syt0r.comicreader.ui.viewer.renderer.image.ImageRenderer
import ua.syt0r.comicreader.ui.viewer.renderer.Renderer
import ua.syt0r.comicreader.ui.viewer.renderer.RendererContainer
import ua.syt0r.comicreader.util.getComponent
import javax.inject.Inject

class ViewerActivity : AppCompatActivity(), ViewerMVP.View {

    @Inject lateinit var presenter: ViewerMVP.Presenter

    private lateinit var renderer: Renderer
    private lateinit var rendererContainer: RendererContainer
    private lateinit var systemUiHelper: SystemUiHelper
    private lateinit var seekBar: SeekBar
    private lateinit var progressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        getComponent(this).inject(this)

        rendererContainer = findViewById(R.id.renderer_container)
        seekBar = findViewById(R.id.chapter_progress)
        progressText = findViewById(R.id.chapter_progress_text)

        //Setup toolbar

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val bottomLayout = findViewById<View>(R.id.bottom_panel)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        systemUiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE,
            SystemUiHelper.FLAG_IMMERSIVE_STICKY, SystemUIVisibilityListener(toolbar, bottomLayout))

        //Setup presenter

        presenter.attachView(this, this)

        if (savedInstanceState == null) {
            systemUiHelper.show()
            presenter.loadFile(intent?.data?.path)
        } else {
            //TODO Restore state
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.viewer_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.options -> OptionsFragment().show(supportFragmentManager,  null)
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setRenderer(rendererType: Int) {

        renderer = when (rendererType) {
            Renderer.PDF -> PdfRenderer(this)
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

        seekBar.setOnSeekBarChangeListener(ViewerSeekBarChangeListener())

    }

    override fun setData(data: Any) {
        renderer.setData(data)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getReadingPosition(): Int = renderer.getReadingPosition()

    override fun scrollToPosition(position: Int) {
        renderer.scrollToPosition(position)
        seekBar.progress = position
    }

    private inner class ViewerSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

        var isTouching = false

        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            progressText.text = "${seekBar?.progress?.plus(1) ?: 0}/${seekBar?.max?.plus(1) ?: 0}"
            if (isTouching)
                renderer.scrollToPosition(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {isTouching = true }

        override fun onStopTrackingTouch(seekBar: SeekBar?) { isTouching = false }

    }



}
