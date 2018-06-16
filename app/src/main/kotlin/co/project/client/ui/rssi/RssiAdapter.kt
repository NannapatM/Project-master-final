package co.project.client.ui.rssi

import android.net.wifi.ScanResult
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.project.client.R
//import co.project.client.data.model.Element

class RssiAdapter: RecyclerView.Adapter<RssiAdapter.RssiViewHolder>() {

    private var wifiList: List<Element>? = null

    override fun onBindViewHolder(holder: RssiViewHolder, pos: Int) {
        holder.setItem(wifiList?.get(pos))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssiViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.items, parent, false)
        view.setOnClickListener {
            
        }
        return RssiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wifiList?.size ?: 0
    }

    fun setList(list: List<ScanResult>?) {
        wifiList = list?.map { item ->
            item
                    .toString()
                    .split(",")
                    .dropLastWhile { it.isEmpty() }
                    .mapIndexed { index, str ->
                        when (index) {
                            0, 2, 3 -> str.split(":").dropLastWhile { it.isEmpty() }[1]
                            else -> null
                        }
                    }
                    .filterNotNull()
                    .let {
                        Element(it[0], it[1], it[2],it[3])
                    }
        }
    }

    class RssiViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private var ssidTxt: AppCompatTextView? = itemView?.findViewById(R.id.tvSSID)
        private var securityTxt: AppCompatTextView? = itemView?.findViewById(R.id.tvSecurity)
        private var strengthTxt: AppCompatTextView? = itemView?.findViewById(R.id.tvLevel)
        private var bssidTxt:AppCompatTextView? = itemView?.findViewById(R.id.tvbssid)

        internal fun setItem(element: Element?) {
            element.let {
                ssidTxt?.text = element?.title
                securityTxt?.text = element?.security
                strengthTxt?.text = "Signal Level: ${element?.level ?: "--"}"
                bssidTxt?.text = element?.bssid
            }
        }
    }

}