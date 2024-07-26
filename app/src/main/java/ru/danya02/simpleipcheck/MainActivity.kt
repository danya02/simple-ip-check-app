package ru.danya02.simpleipcheck

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import ru.danya02.simpleipcheck.databinding.ActivityMainBinding
import java.net.NetworkInterface
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var BACKGROUND: ExecutorService

    private val viewModel: NetworkInterfaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BACKGROUND = Executors.newFixedThreadPool(2)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (item in NetworkInterface.getNetworkInterfaces()) {
            Log.w(TAG, item.name)
        }

        binding.fab.setOnClickListener { _ ->
            viewModel.requestRefresh.postValue(true)
        }

        viewModel.requestRefresh.observe(this) {
            if (it) {
                refreshState()
            }
        }

        viewModel.requestRefresh.postValue(true)
    }


    private fun refreshState() {
//                    Log.w(TAG, "Refreshing sleep")
//                    Thread.sleep(5000L)
//                    Log.w(TAG, "Refreshing")

        lifecycleScope.launch(BACKGROUND.asCoroutineDispatcher()) {
            viewModel.refreshInterfaces()
            Snackbar.make(binding.fab, "Refreshed", Snackbar.LENGTH_SHORT).show()
            viewModel.requestRefresh.postValue(false)
        }
    }

}