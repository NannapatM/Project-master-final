package co.project.client.data.model

import android.os.Parcel
import android.os.Parcelable

class Response(val id: String?, val ssid: String?, val rssi: Int?, val lat: String?, val long: String?, val bssid: String?) : Parcelable {
    constructor(client: Client, ssid: String?, rssi: Int?, lat: String?, long: String?,bssid:String?) : this(client.id, ssid, rssi, lat, long,bssid)
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(ssid)
        writeValue(rssi)
        writeString(lat)
        writeString(long)
        writeString(bssid)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Response> = object : Parcelable.Creator<Response> {
            override fun createFromParcel(source: Parcel): Response = Response(source)
            override fun newArray(size: Int): Array<Response?> = arrayOfNulls(size)
        }
    }
}