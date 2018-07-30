package co.project.client.ui.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import javax.inject.Inject

import co.project.client.data.DataManager
import co.project.client.data.model.Response
import co.project.client.data.model.UpdateParam
import co.project.client.injection.ConfigPersistent
import co.project.client.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@ConfigPersistent

class CompassPresenter<V : CompassMvp.View> @Inject

constructor(private val dataManager : DataManager) : BasePresenter<V>(), CompassMvp.Presenter<V> {

    override var Glat: Double? = null
    override var Glong: Double? = null
    override var currentLocation: Location? = null
    override var Elat: Double = 1.1
    override var Elong: Double = 1.1


   // var Elat: Double? = null
   // var Elong: Double? = null

    override fun post(id: String, rssi: ArrayList<Int>, ssid: String, bssid:String) {
        view.showLoading()
        currentLocation.let {
            if (it == null) {
                view.hideLoading()
                view.showMessage("Current Location is null")
            } else {
                it.latitude = Glat?.let { it }.toString().toDouble()
                it.longitude = Glong?.let { it }.toString().toDouble()
                dataManager.serverService
                        .update(UpdateParam(id, ssid, rssi, it.latitude, it.longitude, bssid))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn { err ->
                            err.printStackTrace()
                            Response("", "", 0,"0.0","0.0", "")
                        }
                        .subscribe(
                                { response ->
                                    Toast.makeText(view.getContext(), "SENT", Toast.LENGTH_SHORT).show()
                                    view.hideLoading()
                                    view.onReceiveDestination(response.lat?.toDouble() ?: 0.0, response.long?.toDouble() ?: 0.0)
                                    Log.d("recLat", "lat = " + response.lat)
                                    Log.d("recLong", "long = " + response.long)
                                    Elat = response.lat.toString().toDouble()
                                    Elong = response.long.toString().toDouble()




                                },
                                { err ->
                                    err.printStackTrace()
                                    view.hideLoading()
                                    view.showMessage(err.message ?: "NOPE!")
                                }
                        )
                //}
                //}
                //   }
            }
        }
    }

    private val alpha = 0.97f

    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
    override fun onSensorChanged(event: SensorEvent?) {

        var azimuth: Float
        val azimuthFix: Float = 0.toFloat()

        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
            }
        }

        val arrR = FloatArray(9)
        val arrI = FloatArray(9)

        if (SensorManager.getRotationMatrix(arrR, arrI, mGravity, mGeomagnetic)) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(arrR, orientation)

           // var lat = lat?.let{it}.toString().toDouble()
           // var long = long?.let{it}.toString().toDouble()


             currentLocation?.let{
                 it.latitude = Glat?.let{it}.toString().toDouble()
                 it.longitude = Glong?.let{it}.toString().toDouble()
                    azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                    azimuth = (azimuth + azimuthFix + 360f) % 360
                    //azimuth -= getBearing(it.latitude, it.longitude, 13.795785515, 100.32620676).toFloat()
                    azimuth -= getBearing(it.latitude, it.longitude, Elat,Elong).toFloat()
                    Log.d("currentLocation", "this is currentLo:  " + it.latitude + "," + it.longitude)
                    Log.d ("EndLocation","End location is :   "+  Elat + "," + Elong)
                    view.adjustArrow(azimuth)
                }
            }
        }




    private fun getBearing(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Double {
        val latitude1 = Math.toRadians(startLat)
        val latitude2 = Math.toRadians(endLat)
        val longDiff = Math.toRadians(endLng - startLng)
        val y = Math.sin(longDiff) * Math.cos(latitude2)
        val x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff)

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360
    }



}