package co.project.client.ui.compass

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import co.project.client.R
import co.project.client.data.local.LocationHelper
import co.project.client.data.model.Client
import co.project.client.ui.base.BaseActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class CompassActivity : BaseActivity(), CompassMvp.View {

    @BindView(R.id.image_view) lateinit var arrowView: AppCompatImageView
    @BindView(R.id.lat_text) lateinit var latText: AppCompatTextView
    @BindView(R.id.long_text) lateinit var longText: AppCompatTextView
    @BindView(R.id.distance_text) lateinit var locationText: AppCompatTextView
    @BindView(R.id.desLat_text) lateinit var desLatText: AppCompatTextView
    @BindView(R.id.desLong_text) lateinit var desLongText: AppCompatTextView

    private var currentAzimuth: Float = 0.toFloat()
    private var client : Client? = null

    internal var mocklist: ArrayList<Int> = ArrayList(10)





    @Inject lateinit var presenter: CompassPresenter<CompassMvp.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compass_page)

        mocklist.add(-32);
        mocklist.add(-36);
        mocklist.add(-31);
        mocklist.add(-32);
        mocklist.add(-33);
        mocklist.add(-34);
        mocklist.add(-45);
        mocklist.add(-38);
        mocklist.add(-37);
        mocklist.add(-39);

        for (z in mocklist.indices) {

            Log.d("mocklist", "   "+mocklist[z])
        }



        client = intent.getParcelableExtra("client")
        setup()


    }

    override fun setup() {
        activityComponent.inject(this)
        unbinder = ButterKnife.bind(this)
        presenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        LocationHelper.instance.registerCompassSensor(this, presenter)
    }

    override fun onPause() {
        LocationHelper.instance.unregisterCompassSensor(this, presenter)
        super.onPause()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    @OnClick(R.id.refreshBtn)
    override fun onGetLocationClicked() {

        val b = intent.extras
        val ssid = b!!.getString("ssid")
        val list = b.getIntegerArrayList("list")
        val bssid = b!!.getString("bssid")

        Log.d("CompassSSID",ssid)
        Log.d("CompassList","this is list"+list)
        Log.d("CompassBSSID",bssid)
        if (LocationHelper.instance.checkPermission(this)) {
            LocationHelper.instance.getLocation(this)?.let {
                presenter.currentLocation = it
                client?.let {
                    presenter.post(it.id, list,ssid,bssid)
                }

                latText.text = "Latitude: ${it.latitude}"
                longText.text = "Longitude: ${it.longitude}"

            }
        }
    }

    override fun onReceiveDestination(lat: Double, long: Double) { //lat: String, long: String
        //bearingText.text = "Bearing: ${it.bearingTo(temp)}"
//        locationText.text = "Distance: ${it.distanceTo(temp)} meters"
        Timber.e("kai ka start here")
        desLatText.text = "Destination Lat: $lat"
        Timber.e("kai kak is here")
       desLongText.text = "Destination Long: $long"

        Log.d("RLat","Lat: $lat")
        Log.d("RLong","Long: $long")
       // desLatText.text ="this is receive: $lat"
      //  Log.d("latkaedok","this is lat: $lat")
    }



    override fun adjustArrow(azimuth: Float) {
        RotateAnimation(
                -currentAzimuth,
                -azimuth,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        ).let {
            it.duration = 500
            it.repeatCount = 0
            it.fillAfter = true
            arrowView.startAnimation(it)
        }
        currentAzimuth = azimuth
    }

}
