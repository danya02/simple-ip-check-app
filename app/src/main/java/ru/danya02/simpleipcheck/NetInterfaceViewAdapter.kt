package ru.danya02.simpleipcheck

import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class NetInterfaceViewAdapter(var netInterfaces: List<NetInterface>) :
    RecyclerView.Adapter<NetInterfaceViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameLabel: TextView = view.findViewById(R.id.interfaceNameLabel)
        val ipAddrTextBox: EditText = view.findViewById(R.id.interfaceAddressTextBox)
        val copyBtn: AppCompatImageButton = view.findViewById(R.id.interfaceCopyBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interface_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = netInterfaces.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val netInterface = netInterfaces[position]
        holder.nameLabel.text = netInterface.name
        holder.ipAddrTextBox.setText(netInterface.address)
        holder.copyBtn.setOnClickListener {
            // Copy to clipboard
            val clipboard =
                it.context.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText(netInterface.address, netInterface.address)
            clipboard.setPrimaryClip(clip)

            // Snackbar show
            val snackbar =
                Snackbar.make(it, "Copied: ${netInterface.address}", Snackbar.LENGTH_SHORT)
            snackbar.show()
        }
    }

}