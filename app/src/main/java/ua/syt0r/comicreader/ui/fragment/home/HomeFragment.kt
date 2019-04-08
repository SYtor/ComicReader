package ua.syt0r.comicreader.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ua.syt0r.comicreader.R

class HomeFragment : Fragment() {

    lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        val historyRecycler = root.findViewById<RecyclerView>(R.id.history_recycler)
        val historyAdapter = PinAdapter()

        historyRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        historyRecycler.adapter = historyAdapter

        viewModel.mutablePins.observe(this, Observer {
            historyAdapter.pins = it
            historyAdapter.notifyDataSetChanged()
        })


        val pinRecycler = root.findViewById<RecyclerView>(R.id.pins_recycler)
        val pinAdapter = PinAdapter()

        pinRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        pinRecycler.adapter = pinAdapter

        viewModel.mutablePins.observe(this, Observer {
            pinAdapter.pins = it
            pinAdapter.notifyDataSetChanged()
        })

        return root
    }


}