package co.project.client.ui.rssi

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import co.project.client.R
import co.project.client.data.model.Client
import co.project.client.ui.base.BaseActivity
import co.project.client.ui.compass.CompassActivity
import co.project.client.ui.main.MainMvp
import co.project.client.ui.main.MainPresenter
import org.w3c.dom.Text

import java.util.ArrayList
import java.util.Date
import java.util.concurrent.Delayed
import javax.inject.Inject

class rssiActivity : AppCompatActivity() {

  //  @BindView(R.id.tvLevel) lateinit var TvLevel: AppCompatTextView
  //  @BindView(R.id.tvSecurity) lateinit var TvSecurity: AppCompatTextView
  //  @BindView(R.id.tvSSID) lateinit var TvSsid:AppCompatTextView
  //@Inject lateinit var presenter: RssiPresenter<RssiMvp.View>

    private var nets = arrayOfNulls<Element>(20)
    private var client : Client? = null
    private var latEdit : String? = null
    private var longEdit: String? = null

    //private var nets: Array<Element>
    private var wifiManager: WifiManager? = null
    private var wifiList: List<ScanResult>? = null
    //internal var dialog: Dialog
    internal var list: ArrayList<String> = ArrayList()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rssi_page)
        val b = intent.extras
   //     val latEdit = b.getDouble("lat")

   //     val longEdit = b.getDouble("long")
        client = intent.getParcelableExtra("client")
        latEdit = intent.getStringExtra("lat")
        longEdit = intent.getStringExtra("long")

        Log.d("clientRssi", "client" + client)
        Log.d("lat","Rssilat:    "+ latEdit)
        Log.d("long","Rssilong:   "+ longEdit)
      //  setup()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)




         //client = intent.getParcelableExtra("client");

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            detectWiFi()
            Snackbar.make(view, "Scanning....", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        }




      /*  val fabCon = findViewById<FloatingActionButton>(R.id.connectionBtn)
        fabCon.setOnClickListener{ view ->
          //  presenter.connectToServer()
            Snackbar.make(view,"connecting to server...", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()

        }*/
    }

   /* override fun setup() {
        activityComponent.inject(this)
        unbinder = ButterKnife.bind(this)
        presenter.attachView(this)
    }*/



    /*public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            mWifiListener.getScanningResults();
        }
    }*/
    /*fun alert(){
        val dialog= ProgressDialog(this)
        dialog.setMessage("Please wait")
        dialog.setTitle("Loading")
        dialog.setCancelable(false)
        dialog.isIndeterminate=true
        dialog.show()
    }*/
    fun detectWiFi() {
        //TODO: Permission!!!
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }else{
            //getScanningResults();
            //do something, permission was previously granted; or legacy device

            this.wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            this.wifiManager.startScan();
            this.wifiList = this.wifiManager.getScanResults();
        }*/
        this.wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        this.wifiManager!!.startScan()
        this.wifiList = this.wifiManager!!.scanResults

        Log.d("TAG", wifiList!!.toString())

        this.nets = arrayOfNulls<Element>(wifiList!!.size)
      //  this.nets = arrayOfNulls<Element>?(9)
        for (i in wifiList!!.indices) {
            val item = wifiList!![i].toString()
            val vector_item = item.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val item_ssid = vector_item[0]
            val item_capabilities = vector_item[2]
            val item_level = vector_item[3]
            val item_bssid = vector_item[1]

            val ssid = item_ssid.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val security = item_capabilities.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val level = item_level.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val bssid = item_bssid

            nets[i] = Element(ssid, security, level,bssid)
        }

        val adapterElements = AdapterElements(this)
        val netlist = findViewById<ListView>(R.id.listItem)
        val info = findViewById<TextView>(R.id.info)
        netlist.adapter = adapterElements
        netlist.setSelector(R.drawable.listselector)
       // Toast.makeText(getApplicationContext(),"Sending",Toast.LENGTH_SHORT).show();


        //  Toast.makeText(MainActivity.this, "This is my Toast message!", Toast.LENGTH_LONG).show();
        netlist.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
           // Toast.makeText(applicationContext,"Sending",Toast.LENGTH_SHORT).show()

            var c = 0
            var k: Int
            val name = nets[i]!!.title
            val bssid = nets[i]!!.bssid
           // ProgressBar.show(this, "Loading", "Wait while loading...");
           /* val dialog= ProgressDialog(this)
            dialog.setMessage("Please wait")
            dialog.setTitle("Loading")
            dialog.setCancelable(false)
            dialog.isIndeterminate=true
            dialog.show()*/

            //alert()

            while (c < 10) {

                k = 0
                while (k < wifiList!!.size && name.compareTo(nets[k]!!.title) != 0) {

                    //Toast.makeText(this, nets[k]!!.level + " selected", Toast.LENGTH_LONG).show();
                    k++
                }

                list.add(nets[k]!!.level)

             //   Toast.makeText(getApplicationContext(),"Loading..." + c, Toast.LENGTH_LONG).show();
                //   String name = nets[i].getTitle();

                Log.d("round", "roundc" + c + nets[k]!!.title + nets[k]!!.level)


               // Toast.makeText(getApplicationContext(),"Sending.....",Toast.LENGTH_SHORT).show();
                c++
                SystemClock.sleep(2000)
                // Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_SHORT).show();
                detectWiFi()


                //  Toast.makeText(MainActivity.this,"Send", Toast.LENGTH_SHORT).show();
            }
            // Toast.makeText(MainActivity.this,"Send", Toast.LENGTH_SHORT).show();

            for (z in list.indices) {

                Log.d("list", "    "+ z + list[z])
            }

            Log.d("wifiInfo","this is wifi info:   "+wifiList)

            this.client?.let {
                val intent = Intent(this, CompassActivity::class.java)
                intent.putExtra("ssid",name)
                Log.d("clientSsid",name)
                intent.putStringArrayListExtra("list",list)
                Log.d("clientList","list"+list)
                intent.putExtra("bssid",bssid)
               // intent.putExtra("rssiList",list)
                intent.putExtra("client", this.client)
                Log.d("clientIntent","param:  "+ this.client)
                intent.putExtra("lat",latEdit)
                Log.d("clientLat","this is lat" + latEdit)
                intent.putExtra("long",longEdit)
                Log.d("clientLong","this is long" + latEdit)

                startActivity(intent)
            }

         /*   val move = Intent(this@MainActivity, Main2Activity::class.java)
            move.putExtra("demo", "demo")
            startActivity(move)*/
            list.clear()
        }


    }

    /*fun move(view: View) {
        val move = Intent(this@MainActivity, Main2Activity::class.java)

        move.putExtra("demo", "demo")
        startActivity(move)
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    internal inner class AdapterElements(var context: Activity) : ArrayAdapter<Any>(context, R.layout.items, nets) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val item = inflater.inflate(R.layout.items, null)

            //val tvSsid = inflater.inflate(R.id.tvSSID,null)
            val tvSsid = item.findViewById<TextView>(R.id.tvSSID)
            tvSsid.text = nets[position]!!.title

           // val tvSecurity = item.findViewById(R.id.tvSecurity) as TextView
            val tvSecurity = item.findViewById<TextView>(R.id.tvSecurity)
            tvSecurity.text = nets[position]!!.security

            //val tvLevel = item.findViewById(R.id.tvLevel) as TextView
            val tvLevel = item.findViewById<TextView>(R.id.tvLevel)
            tvLevel.text = "Signal Level: " + nets[position]!!.level

            val tvbssid = item.findViewById<TextView>(R.id.tvbssid)
            tvbssid.text = nets[position]!!.bssid

            return item
        }
    }



    private fun connectToWifi(networkSSID: String, networkPassword: String) {
        if (!wifiManager!!.isWifiEnabled) {
            wifiManager!!.isWifiEnabled = true
        }
        val conf = WifiConfiguration()
        conf.SSID = String.format("\"%s\"", networkSSID)
        conf.preSharedKey = String.format("\"%s\"", networkPassword)
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA)

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //Toast.makeText(MainActivity.this, "Name of wifi: " + networkSSID + " Password " + networkPassword, Toast.LENGTH_SHORT).show();


        val netId = wifiManager.addNetwork(conf)

        val list = wifiManager.configuredNetworks
        for (i in list) {
            if (i.SSID != null && i.SSID == "\"" + networkSSID + "\"") {
                wifiManager.disconnect()
                wifiManager.enableNetwork(i.networkId, true)
                wifiManager.reconnect()

                break
            }
        }

    }


}