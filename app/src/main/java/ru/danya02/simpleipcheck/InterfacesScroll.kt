package ru.danya02.simpleipcheck

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class InterfacesScroll : Fragment() {
    private val TAG = "InterfacesScroll"

    private lateinit var recyclerView: RecyclerView
    private val viewModel: NetworkInterfaceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        viewModel.netInterfaces.observe(viewLifecycleOwner, Observer {
            val adapter = recyclerView.adapter

            if (adapter is NetInterfaceViewAdapter) {
                if (adapter.netInterfaces != it) {
                    Log.i(TAG, "NetInterfaces changed")
                    adapter.netInterfaces = it
                    adapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.showIsRefreshing.observe(viewLifecycleOwner, Observer {
            view.isRefreshing = it
        })

        view.setOnRefreshListener {
            viewModel.requestRefresh.postValue(true)
        }
        return view
    }
}