package co.project.client.ui.rssi

import android.hardware.SensorEventListener
import android.location.Location
import co.project.client.ui.base.BaseMvp

interface RssiMvp {
    interface View: BaseMvp.View {

    }
    interface Presenter<in V: BaseMvp.View>: BaseMvp.Presenter<V>, SensorEventListener {
        fun post(rssi: ArrayList<Int>)
    }
}
