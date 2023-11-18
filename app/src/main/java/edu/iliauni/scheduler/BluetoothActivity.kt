package edu.iliauni.scheduler

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"
private const val PERMISSION_ALL = 0
private const val TAG = "BluetoothActivity"

class BluetoothActivity : AppCompatActivity() {

    var adapter = BluetoothAdapter.getDefaultAdapter()
    var device_list = arrayListOf<String>()
    var discoverable: Boolean = false
    var btnClear: Button? = null
    var btnScan: Button? = null
    var btnDiscoverable: Button? = null
    var txtProgress: TextView? = null
    var txtJSON: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        btnDiscoverable = findViewById(R.id.btn_discoverable)
        btnScan = findViewById(R.id.btn_scan)
        btnClear = findViewById(R.id.btn_clear)
        txtProgress = findViewById(R.id.txt_progress)
        txtJSON = findViewById(R.id.txt_json)

        btnDiscoverable?.setOnClickListener {
            val txtInfo = findViewById<TextView>(R.id.txt_info)
            discoverable = !discoverable
            if(discoverable)
                txtInfo.text = "Discoverable mode is on"
            else
                txtInfo.text = "Discoverable mode is off"

            askForDiscoverable()
        }

        btnScan?.setOnClickListener {
            txtProgress?.text = "Scanning..."
            btnScan?.isEnabled = false
            ScanDevices()
        }

        btnClear?.setOnClickListener {
            btnScan?.isEnabled = true
            btnClear?.isEnabled = false
            txtJSON?.text = ""
            txtProgress?.text = "Click on 'SCAN' button to find nearby devices..."
            device_list.clear()
        }

        checkBluetoothPermissions()
    }

    @SuppressLint("MissingPermission")
    private fun askForDiscoverable(){
        adapter.name = "SchedulerAndroid"
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600)
        }
        startActivityForResult(discoverableIntent, 1)
    }

    @SuppressLint("NewApi")
    private fun checkBluetoothPermissions(){
        val permissions = arrayOf(ACCESS_FINE_LOCATION)
        if (!hasPermissions(this, *permissions)) {
            requestPermissions(permissions, PERMISSION_ALL)
            return
        }
        else {
            btnDiscoverable?.isEnabled = true
            btnScan?.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun ScanDevices() {
        if (!adapter.isEnabled) {
            // Bluetooth is not enabled
            Log.d(TAG, "Bluetooth is not enabled")
            txtProgress?.text = "Bluetooth is not enabled. Please turn it on and continue later"
            btnScan?.isEnabled = true
        } else {
            // Bluetooth is enabled
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            registerReceiver(receiver, filter)
            adapter.startDiscovery()
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ALL -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "permission granted")
                    btnDiscoverable?.isEnabled = true
                    btnScan?.isEnabled = true
                } else {
                    txtProgress?.text = "Permission not granted..."
                    Log.e(TAG, "permission not granted")
                }
            }
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action!!
            when(action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    txtProgress?.text = "Discovery Started..."
                    Log.d(TAG, "Discovery Started")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name +":"+ device.address
                    if(!device_list.contains(deviceName)){
                        device_list.add(deviceName)
                        Log.d(TAG, "Found Device: " + deviceName)
                        txtJSON?.text = txtJSON?.text.toString() + "\n" + deviceName
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    txtProgress?.text = "Discovery Finished..."
                    btnClear?.isEnabled = true
                    Log.d(TAG, "Discovery Finished")
                }
            }
        }
    }

    override fun onDestroy() {
        device_list.clear()
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}