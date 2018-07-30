package co.project.client.ui.main

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import co.project.client.R
import co.project.client.data.SyncService
import co.project.client.data.local.LocationHelper
import co.project.client.data.model.Client
import co.project.client.injection.module.ApiModule
import co.project.client.ui.base.BaseActivity
import co.project.client.ui.compass.CompassActivity
import co.project.client.ui.rssi.rssiActivity
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvp.View {

    companion object {
        val EXTRA_TRIGGER_SYNC_FLAG =
                "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG"
    }

    @Inject lateinit var presenter: MainPresenter<MainMvp.View>

    @BindView(R.id.reset_btn) lateinit var resetBtn: AppCompatButton
    @BindView(R.id.connect_btn) lateinit var connectBtn: AppCompatButton
    @BindView(R.id.connection_text) lateinit var connectionTxt: AppCompatTextView
    @BindView(R.id.lat_text) lateinit var latText: AppCompatTextView
    @BindView(R.id.long_text) lateinit var longText: AppCompatTextView
    @BindView(R.id.editLat) lateinit var editLatText: AppCompatEditText
    @BindView(R.id.editLong) lateinit var editLongText: AppCompatEditText

    var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)

        if (intent.getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this))
        }
        setup()


    }

    override fun setup() {
        unbinder = ButterKnife.bind(this)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    @OnClick(R.id.connect_btn)
    fun onConnectBtnClicked() {
        presenter.connectToServer()
    }

    @OnClick(R.id.reset_btn)
    fun onResetBtnClicked() {
        presenter.resetServerConnection()
    }

    @OnClick(R.id.get_location_btn)
    fun onGetLocationClicked() {
        if (LocationHelper.instance.checkPermission(this)) {
            LocationHelper.instance.getLocation(this)?.let {
                latText.text = "Latitude: ${it.latitude}"
                longText.text = "Longitude: ${it.longitude}"
                editLatText.setText("${it.latitude}")
                editLongText.setText("${it.longitude}")

                val temp = Location(android.location.LocationManager.GPS_PROVIDER)
                temp.latitude = 37.418436
                temp.longitude = -121.963477
            }
        }
    }

    //@OnClick(R.id.compass_page)
   /* fun onCompassPage(){
        this.client?.let {
            val intent = Intent(this, CompassActivity::class.java)
            intent.putExtra("client", this.client)


            Log.d("CompassClient","this is pass value"+this.client)
            startActivity(intent)
        }
    }*/

    @OnClick(R.id.rssi_page)
    fun onNetPage(){

        this.client?.let {
            val intents = Intent(this, rssiActivity::class.java)
            val latUser: String = editLatText.text.toString()
            val longUser: String = editLongText.text.toString()
            Log.d("Net", "param   " + this.client)
            intents.putExtra("client", this.client)
            intents.putExtra("lat",latUser)
            intents.putExtra("long",longUser)
            Log.d("latUser","this is latUser :   " + latUser)
            Log.d("longUser","this is longUser :   " + longUser)
            startActivity(intents)
        }

    }

    override fun onConnected(client: Client) {
        connectionTxt.text = "ID: ${client.id}"
        this.client = client
    }

    override fun onServerConnectionResetted() {
        connectionTxt.text = "Connection Reset"
        this.client = null
    }


}
