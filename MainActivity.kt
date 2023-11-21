package com.example.qrapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}