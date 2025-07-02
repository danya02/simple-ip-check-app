package ru.danya02.simpleipcheck

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * A fragment representing a list of Items.
 */
class InterfacesScroll : Fragment() {
    private val TAG = "InterfacesScroll"

    private lateinit var recyclerView: RecyclerView
    private val viewModel: NetworkInterfaceViewModel by activityViewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =
            inflater.inflate(R.layout.interface_list, container, false) as SwipeRefreshLayout

        recyclerView = view.findViewById(R.id.interfaceRecyclerView)

        // Set the adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NetInterfaceViewAdapter(viewModel.getInterfaces())

        viewModel.netInterfaces.observe(viewLifecycleOwner) {
            val adapter = recyclerView.adapter

            if (adapter is NetInterfaceViewAdapter) {
                if (adapter.netInterfaces != it) {
                    Log.i(TAG, "NetInterfaces changed")
                    adapter.netInterfaces = it
                    adapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.showIsRefreshing.observe(viewLifecycleOwner) {
            view.isRefreshing = it
        }

        view.setOnRefreshListener {
            viewModel.requestRefresh.postValue(true)
        }
        return view
    }
}