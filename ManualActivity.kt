package com.example.qrapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class ManualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        var tvContent = findViewById<TextView>(R.id.tvContent)
        tvContent.text = getString(R.string.content_manual_part1, "\t\t","\n\n")
    }



    // identify toolbar markup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.icon_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {  // if the home note is pressed,  go back to MainActivity
                val intent = Intent(this, MainActivity ::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}