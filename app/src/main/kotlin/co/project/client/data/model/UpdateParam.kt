package co.project.client.data.model

import com.google.gson.annotations.SerializedName

class UpdateParam(
        val id: String,
        val ssid: String,
        val rssi: ArrayList<Int>,
        val lat: Double,
//        @SerializedName("this_is_how_to_fucking_rename_this_thing")
        val long: Double,
        val bssid: String
)