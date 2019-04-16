package ua.syt0r.comicreader.ui.fragment.history

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R

class HistoryFragment : Fragment() {

    lateinit var viewModel: HistoryViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler)

        recyclerView.layoutManager = GridLayoutManager(context,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 3 else 5)

        val adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        viewModel.historyRecords.observe(this, Observer {
            adapter.historyRecords = it
            adapter.notifyDataSetChanged()
        })

        return root
    }

}