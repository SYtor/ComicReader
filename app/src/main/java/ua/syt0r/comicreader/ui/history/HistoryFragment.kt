package ua.syt0r.comicreader.ui.history

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.util.NavigationUtils
import ua.syt0r.comicreader.util.OnDBFileClickListener
import ua.syt0r.comicreader.util.getComponent
import java.io.File
import javax.inject.Inject

class HistoryFragment : Fragment(), HistoryMVP.View {

    @Inject lateinit var presenter: HistoryMVP.Presenter

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val navController = NavHostFragment.findNavController(this)

        getComponent(activity!!).inject(this)

        recyclerView = root.findViewById(R.id.recycler)
        recyclerView.layoutManager = GridLayoutManager(context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5)

        adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        val onDBFileClickListener = object : OnDBFileClickListener {
            override fun onClick(dbFile: DbFile) {
                NavigationUtils.navigate(File(dbFile.path), navController, root.context)
            }
        }
        adapter.onDBFileClickListener = onDBFileClickListener

        presenter.attachView(this, this)
        presenter.loadHistory()

        return root
    }

    override fun showHistory(history: List<DbFile>) {
        adapter.historyRecords = history
        adapter.notifyDataSetChanged()
    }

    override fun showEmptyHistory() {

    }

}