package co.tryterra.terrartandroiddemo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import co.tryterra.terrartandroid.TerraRT
import co.tryterra.terrartandroid.enums.Connections
import co.tryterra.terrartandroid.enums.DataTypes

@SuppressLint("UseSwitchCompatOrMaterialCode")
class MainActivity : AppCompatActivity(){
    private lateinit var terraRT: TerraRT
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)

    private lateinit var googleFitConnect: Button
    private lateinit var bleConnect: Button
    private lateinit var wearOSButton: Button
    private lateinit var sensorButton: Button
    private lateinit var antButton: Button


    private var googleConnected: Boolean = false
    private var bleConnected: Boolean = false
    private var wearOsConnected: Boolean = false
    private var sensorConnected: Boolean = false
    private var antConnected: Boolean = false

    private lateinit var wearOsSwitch: Switch
    private lateinit var bleSwitch: Switch
    private lateinit var googleSwitch: Switch
    private lateinit var sensorSwitch: Switch
    private lateinit var antSwitch: Switch

    @SuppressLint("SetTextI18n")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        terraRT = TerraRT(
            DEVID,
            this,
            "testingReferenceId"
        ){
            //Generate an SDK token: https://docs.tryterra.co/reference/generate-authentication-token
            terraRT.initConnection("TOKEN"){ }
        }
        googleFitConnect = findViewById(R.id.google_fit)
        bleConnect = findViewById(R.id.ble)
        wearOSButton = findViewById(R.id.wearos)
        sensorButton = findViewById(R.id.sensor)
        antButton = findViewById(R.id.ant)


        googleSwitch = findViewById(R.id.streamGoogle)
        wearOsSwitch = findViewById(R.id.streamwOS)
        bleSwitch = findViewById(R.id.streamBLE)
        sensorSwitch = findViewById(R.id.streamSensor)
        antSwitch = findViewById(R.id.antSwitch)


        antSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                GenerateUserToken(XAPIKEY, DEVID, terraRT.getUserId()!!).getAuthToken {
                    terraRT.startRealtime(Connections.ANT, it!!, setOf(DataTypes.RR_INTERVAL))
                }
            }
            else{
                terraRT.stopRealtime(Connections.ANT)
            }
        }

        bleSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                GenerateUserToken(XAPIKEY, DEVID, terraRT.getUserId()!!).getAuthToken {
                    terraRT.startRealtime(Connections.BLE, it!!, setOf(DataTypes.HEART_RATE, DataTypes.STEPS))
                }
            }
            else{
                terraRT.stopRealtime(Connections.BLE)
            }
        }


        wearOsSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                GenerateUserToken(XAPIKEY, DEVID, terraRT.getUserId()!!).getAuthToken {
                    terraRT.startRealtime(Connections.WEAR_OS, it!!, setOf(DataTypes.HEART_RATE))
                }
            }
            else{
                terraRT.stopRealtime(Connections.WEAR_OS)
            }
        }

        sensorSwitch.setOnCheckedChangeListener { _, b ->
            if (b){
                GenerateUserToken(XAPIKEY, DEVID, terraRT.getUserId()!!).getAuthToken {
                    terraRT.startRealtime(Connections.ANDROID, it!!, setOf(DataTypes.GYROSCOPE, DataTypes.ACCELERATION))
                }
            }
            else{
                terraRT.stopRealtime(Connections.ANDROID)
            }
        }


        antButton.setOnClickListener {
            if (antConnected){
                terraRT.disconnect(Connections.ANT)
                antButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                antConnected = false
            }else {
                terraRT.startAntPlusScan{
                    if (it) {
                        antButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                        antConnected = true
                    }
                }

            }
        }


        bleConnect.setOnClickListener {
            if (bleConnected){
                terraRT.disconnect(Connections.BLE)
                bleConnect.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                bleConnected = false
            }
            else {
                terraRT.startBluetoothScan(Connections.BLE) {
                    if (it) {
                        bleConnect.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                    }
                }
                bleConnected = true
            }
        }

        sensorButton.setOnClickListener {
            if (sensorConnected){
                terraRT.disconnect(Connections.ANDROID)
                sensorButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                sensorConnected = false
            }
            else {
                sensorButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_off, null)
                sensorConnected = true
            }
        }

        wearOSButton.setOnClickListener {
            if (wearOsConnected){
                terraRT.disconnect(Connections.WEAR_OS)
                wearOSButton.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.button_on, null)
                wearOsConnected = false
            }else {
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
        var resource: Connections? = Connections.BLE
    }
}