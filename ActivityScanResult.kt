package com.example.qrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class ActivityScanResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_result)

        val message = intent.getStringExtra("${R.string.key_value}")
        val text_res = findViewById<TextView>(R.id.res)

        text_res.text = message
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