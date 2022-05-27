package co.tryterra.terrartandroiddemo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import co.tryterra.terrartandroid.Connections
import co.tryterra.terrartandroid.DataTypes
import co.tryterra.terrartandroid.TerraRT


@SuppressLint("UseSwitchCompatOrMaterialCode")
class MainActivity : AppCompatActivity(){
    private lateinit var terraRT: TerraRT
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var googleFitConnect: Button
    private lateinit var polarConnect: Button
    private lateinit var wahooButton: Button
    private lateinit var wearOSButton: Button

    private var googleConnected: Boolean = false
    private var polarConnected: Boolean = false
    private var wahooConnected: Boolean = false
    private var wearOsConnected: Boolean = false


    private lateinit var wearOsSwitch: Switch
    private lateinit var wahooSwitch: Switch
    private lateinit var polarSwitch: Switch
    private lateinit var googleSwitch: Switch



    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        terraRT = TerraRT(
            userID,
            XAPIKEY,
            DEVID,
            this,
            "testingRealTimeData",
        )
        googleFitConnect = findViewById(R.id.google_fit)
        polarConnect = findViewById(R.id.polar)
        wahooButton = findViewById(R.id.wahoo)
        wearOSButton = findViewById(R.id.wearos)


        wahooSwitch = findViewById(R.id.streamWahoo)
        googleSwitch = findViewById(R.id.streamGoogle)
        wearOsSwitch = findViewById(R.id.streamwOS)
        polarSwitch = findViewById(R.id.streamPolar)



        wahooSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                terraRT.startRealtime(Connections.WAHOO, DataTypes.HEART_RATE)
            }
            else{
                terraRT.stopRealtime(Connections.WAHOO)
            }
        }

        polarSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                terraRT.startRealtime(Connections.POLAR, DataTypes.HEART_RATE)
            }
            else{
                terraRT.stopRealtime(Connections.POLAR)
            }
        }

        googleSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                terraRT.startRealtime(Connections.GOOGLE_FIT, DataTypes.STEPS)
            }
            else{
                terraRT.stopRealtime(Connections.GOOGLE_FIT)
            }
        }

        wearOsSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                terraRT.startRealtime(Connections.WEAR_OS, DataTypes.HEART_RATE)
            }
            else{
                terraRT.stopRealtime(Connections.WEAR_OS)
            }
        }


        googleFitConnect.setOnClickListener {
            if (googleConnected){
                terraRT.disconnect(Connections.GOOGLE_FIT)
                googleFitConnect.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                googleConnected = false
            }else {
                terraRT.initConnection(Connections.GOOGLE_FIT, this)
                googleFitConnect.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                googleConnected = true
            }
        }

        polarConnect.setOnClickListener {
            if (polarConnected){
                terraRT.disconnect(Connections.POLAR)
                polarConnect.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                polarConnected = false
            }
            else {
                terraRT.initConnection(Connections.POLAR, this)
                terraRT.startBluetoothScan(Connections.POLAR) {
                    if (it) {
                        polarConnect.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                    }
                }
                polarConnected = true
            }
        }

        wahooButton.setOnClickListener {
            if(wahooConnected){
                terraRT.disconnect(Connections.WAHOO)
                wahooButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                wahooConnected = false
            }else {
                terraRT.initConnection(Connections.WAHOO, this)
                terraRT.startBluetoothScan(Connections.WAHOO) {
                    if (it) {
                        wahooButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                    }
                }
                wahooConnected = true
            }
        }

        wearOSButton.setOnClickListener {
            if (wearOsConnected){
                terraRT.disconnect(Connections.WEAR_OS)
                wearOSButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                wearOsConnected = false
            }else {
                terraRT.initConnection(Connections.WEAR_OS, this)
                terraRT.startBluetoothScan(Connections.WEAR_OS) {
                    if (it) {
                        wearOSButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                    }
                }
                wearOsConnected = true
            }
        }

    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    public override fun onResume(){
        super.onResume()
    }


    companion object{
        const val TAG = "Terra"
        var resource: Connections? = Connections.WAHOO
    }
}