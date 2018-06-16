package co.project.client.data.model

import android.os.Parcel
import android.os.Parcelable

class Client(val id: String, val desLat: Double, val desLon: Double) : Parcelable {
    constructor(source: Parcel) : this(source.readString(), source.readDouble(), source.readDouble())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeDouble(desLat)
        writeDouble(desLon)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Client> = object : Parcelable.Creator<Client> {
            override fun createFromParcel(source: Parcel): Client = Client(source)
            override fun newArray(size: Int): Array<Client?> = arrayOfNulls(size)
        }
    }
}