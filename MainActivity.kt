package com.example.qrapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickGoGenerate(view: View){
        val intent = Intent(this, ActivityGenerateCodeTest::class.java)
        startActivity(intent)
    }

   fun onClickGoScan(view: View){
        val intent = Intent(this, ActivityScanCodeTest ::class.java)
        //val intent = Intent(this, ActivityScanResult ::class.java)
        startActivity(intent)
    }

    // identify toolbar markup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.icon_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.note -> {  // if the home note is pressed,  go back to NoteActivity
                val intent = Intent(this, ActivityNote ::class.java)
                startActivity(intent)
            }
        }

        when(item.itemId){
            R.id.manual -> {  // if the home note is pressed,  go back to NoteActivity
                val intent = Intent(this, ManualActivity ::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}