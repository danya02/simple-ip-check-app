package ru.danya02.simpleipcheck

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.danya02.simpleipcheck.databinding.ActivityMainBinding
import java.net.NetworkInterface

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (item in NetworkInterface.getNetworkInterfaces()) {
            Log.w(TAG, item.name)
        }

        binding.fab.setOnClickListener { view ->
            refreshState()
        }

        recyclerView = findViewById(R.id.interfaceRecyclerView)
        refreshState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refreshState() {
        var netInterfaces = mutableListOf<NetInterface>();

        for (item in NetworkInterface.getNetworkInterfaces()) {
            for (addr in item.inetAddresses) {
                var ip = addr.hostAddress;
                if (ip == null) {
                    ip = "(no ip)";
                }
                netInterfaces.add(NetInterface(item.name, ip))
            }
        }

        Log.i(TAG, netInterfaces.toString())
        this.recyclerView.adapter = NetInterfaceViewAdapter(netInterfaces)
        this.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        Snackbar.make(binding.fab, "Refreshed", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .setAnchorView(R.id.fab).show()

    }
}