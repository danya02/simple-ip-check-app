package ru.danya02.simpleipcheck

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import java.net.NetworkInterface
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkInterfaceViewModel : ViewModel() {
    fun getInterfaces(): List<NetInterface> {
        return netInterfaces.value!!.toList()
    }

    private val TAG = "NetworkInterfaceViewModel"

    val netInterfaces = MutableLiveData<MutableList<NetInterface>>(mutableListOf())

    val showIsRefreshing = MutableLiveData(false)

    val requestRefresh = MutableLiveData(false)

    suspend fun refreshInterfaces() {
        return withContext(Dispatchers.IO) {
            showIsRefreshing.postValue(true)

            val ifaces = mutableListOf<NetInterface>()
            ifaces.add(NetInterface("External IP", "(loading...)"))
//        Log.w("111", "Refreshing sleep before getNetworkInterfaces")
//        Thread.sleep(5000L)
//        Log.w("111", "Refreshing before getNetworkInterfaces")


            Log.i(TAG, "Starting native getNetworkInterfaces")
            val rawIfaces = runInterruptible { NetworkInterface.getNetworkInterfaces() }
            for (item in rawIfaces) {
                val addresses = runInterruptible { item.inetAddresses }
                for (addr in addresses) {
                    var ip = runInterruptible { addr.hostAddress }
                    if (ip == null) {
                        ip = "(no ip)"
                    }
                    ifaces.add(NetInterface(item.name, ip))
                }
            }
            Log.i(TAG, "Finished native getNetworkInterfaces")

            netInterfaces.postValue(ifaces)

            // Get IP from icanhazip.com
//        Log.w("111", "Refreshing sleep before icanhazip.com")
//        Thread.sleep(5000L)
//        Log.w("111", "Refreshing before icanhazip.com")

            val newIfaces = ifaces.toMutableList()
            var ip: String
            try {
                val url = URL("https://icanhazip.com")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                con.connect()

                ip = con.inputStream.bufferedReader().readText().trim()
            } catch (e: Exception) {
                e.printStackTrace()
                ip = "(FAIL: ${e.message})"
            }

            newIfaces[0] = NetInterface("External IP", ip)

            Log.i(TAG, "Finished icanhazip.com, posting newIfaces")
            netInterfaces.postValue(newIfaces)
            showIsRefreshing.postValue(false)

        }
    }
}