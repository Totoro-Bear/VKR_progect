package com.example.qrapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qrapp.databinding.ActivityScanCodeTestBinding


class ActivityScanCodeTest : AppCompatActivity() {
    private lateinit var binding: ActivityScanCodeTestBinding
    private lateinit var codeScanner: CodeScanner  // Code scanner, lets make camera settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanCodeTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // correlate CodeScanner with the camera
        codeScanner = CodeScanner(this, binding.scannerVideo)

        //start the window, pass the permission that needs to be checked
        activityResultLauncher.launch(arrayOf(android.Manifest.permission.CAMERA))
    }

    // if the window is working again, restart the camera
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    // if the window is minimized, stop the camera to free up resources
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()

    }

    private  fun setupQRScanner(){

        // scan parameters
        codeScanner.camera = CodeScanner.CAMERA_BACK  // camera selection(back/font/number_camera)
        codeScanner.formats = CodeScanner.ALL_FORMATS  // type of recognized code
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // type of autofocus (with interval/continuous)
        codeScanner.scanMode = ScanMode.SINGLE  // scan mode
        codeScanner.isAutoFocusEnabled = true // the status of the autofocus in moment start camera
        codeScanner.isFlashEnabled = false  // the status of the flashlight in moment start camera

        // if correct
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "${resources.getString(R.string.result)} ${it.text}",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ActivityScanResult::class.java)
                intent.putExtra("${R.string.key_value}", it.text)
                startActivity(intent)
            }
        }

        // if the error
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "${resources.getString(R.string.error)} ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.startPreview()
    }

    private val activityResultLauncher =
        // method RequestMultiplePermissions() returns the map, when
        // the key is the name of the resolution, the value is the result
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions ->
            //Log.d("my", "${permissions}")
            //Log.d("my", "${permissions.keys}")
            permissions.entries.forEach {
                val permissionsName = it.key
                val permissionStatus = it.value
                //Log.d("my", "${permissionsName}, ${permissionStatus}")
                if (permissionStatus) {
                    setupQRScanner()
                } else {
                    Toast.makeText(this, resources.getString(R.string.no_permissions), Toast.LENGTH_SHORT).show()
                }
            }
        }

    // identify toolbar markup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.icon_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {finish()}  // if the home button is pressed,  go back to MainActivity
        }
        return true
    }

}

