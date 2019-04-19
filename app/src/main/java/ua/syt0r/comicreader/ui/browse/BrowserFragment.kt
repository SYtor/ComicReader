package ua.syt0r.comicreader.ui.browse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.ui.viewer.ViewerActivity
import ua.syt0r.comicreader.util.getComponent
import java.io.File
import javax.inject.Inject

class BrowserFragment : Fragment(), BrowseMVP.View {

    @Inject lateinit var presenter: BrowseMVP.Presenter

    private lateinit var adapter: FolderAdapter
    private lateinit var currentFolderText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_browse, container, false)

        getComponent(activity!!).inject(this)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = FolderAdapter()
        recyclerView.adapter = adapter

        currentFolderText = root.findViewById(R.id.parent_folder_text)
        progressBar = root.findViewById(R.id.progress)

        adapter.onFileSelectListener = object : FolderAdapter.OnFileSelectListener {
            override fun onFileSelected(isUpDirSelected: Boolean, file: File) {

                if (file.isFile) {
                    context?.startActivity(Intent(context, ViewerActivity::class.java).apply {
                        data = Uri.fromFile(file)
                    })
                } else {
                    presenter.updateList(file, isUpDirSelected)
                }
            }
        }

        adapter.onFileLongClickListener = object : FolderAdapter.OnFileLongClickListener {
            override fun onFileLongClick(file: File) {
                presenter.pinFile(file)
            }
        }

        presenter.attachView(this, this)
        presenter.updateList(null, false)

        return root
    }

    override fun setProgressBarLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun updateList(isTopLevelDir: Boolean, files: List<File>) {
        adapter.files = files
        adapter.isTopLevelDir = isTopLevelDir
        adapter.notifyDataSetChanged()
    }
}