package ua.syt0r.comicreader.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.database.entity.DbFile
import ua.syt0r.comicreader.util.NavigationUtils
import ua.syt0r.comicreader.util.OnDBFileClickListener
import ua.syt0r.comicreader.util.getComponent
import java.io.File
import javax.inject.Inject

class HomeFragment : Fragment() , HomeMVP.View {

    @Inject lateinit var presenter: HomeMVP.Presenter

    lateinit var historyRecycler: RecyclerView
    lateinit var historyAdapter: HorizontalAdapter
    lateinit var emptyHistoryView: View

    lateinit var pinRecycler: RecyclerView
    lateinit var pinAdapter: HorizontalAdapter
    lateinit var emptyPinsView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val navController = NavHostFragment.findNavController(this)

        getComponent(activity!!).inject(this)

        historyRecycler = root.findViewById(R.id.history_recycler)
        historyAdapter = HorizontalAdapter()
        historyRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        historyRecycler.adapter = historyAdapter
        emptyHistoryView = root.findViewById(R.id.empty_history_text)

        pinRecycler = root.findViewById(R.id.pins_recycler)
        pinAdapter = HorizontalAdapter()
        pinRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        pinRecycler.adapter = pinAdapter
        emptyPinsView = root.findViewById(R.id.empty_pins_text)

        val onDBFileClickListener = object : OnDBFileClickListener {
            override fun onClick(dbFile: DbFile) {
                NavigationUtils.navigate(File(dbFile.path), navController, root.context)
            }
        }

        historyAdapter.onDBFileClickListener = onDBFileClickListener
        pinAdapter.onDBFileClickListener = onDBFileClickListener

        presenter.attachView(this, this)
        presenter.loadData()

        return root
    }


    override fun showPins(pins: List<DbFile>) {
        pinRecycler.visibility = View.VISIBLE
        emptyPinsView.visibility = View.GONE
        pinAdapter.list = pins
        pinAdapter.notifyDataSetChanged()
    }

    override fun showNoPins() {
        pinRecycler.visibility = View.INVISIBLE
        emptyPinsView.visibility = View.VISIBLE
    }

    override fun showHistory(history: List<DbFile>) {
        historyRecycler.visibility = View.VISIBLE
        emptyHistoryView.visibility = View.GONE
        historyAdapter.list = history
        historyAdapter.notifyDataSetChanged()
    }

    override fun showNoHistory() {
        historyRecycler.visibility = View.INVISIBLE
        emptyHistoryView.visibility = View.VISIBLE
    }

}