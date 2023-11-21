package com.example.qrapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ActivityScanResult : AppCompatActivity() {

    private var status = ""
    private var apiKey = "330f3e566b7dd0b52a13b342bab463159473559684c3a2f4559f4441371df88e"
    private var url = "https://www.virustotal.com/api/v3/urls"
    private var client = OkHttpClient()
    //private val checkURL = "https://stackoverflow.com/questions/2989284/what-is-the-max-size-of-localstorage-values"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_result)

        var checkURL = intent.getStringExtra("${R.string.key_value}")

        var textResult = findViewById<TextView>(R.id.tvResult)
        textResult.text = checkURL

        val requestBody = FormBody.Builder()
            .add("url", checkURL.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("x-apikey", apiKey)
            .post(requestBody)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                Log.d("test", "$request")
                val jsonResponse = response.body?.string()

                // JSON-file contains the type of operation, the object ID, and a link to the analysis.
                val jsonObject = JSONObject(jsonResponse)
                if (jsonResponse != null) {
                    Log.d("response", jsonResponse)
                }
                // get a link to the analysis
                val finalURL =
                    jsonObject.getJSONObject("data").getJSONObject("links").getString("self")

                // calling the function for the next request
                Log.d("prob", finalURL)
                secondRequest(finalURL)
            }

            catch (e: IOException)
            {
                Log.d("error", "$e")
                e.printStackTrace()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun secondRequest(url2: String) {

        val request = Request.Builder()
            .url(url2)
            .addHeader("x-apikey", apiKey)
            .get()
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val jsonResponse = response.body!!.string()

                //parsing the JSON file
                val jsonObject = JSONObject(jsonResponse)
                Log.d("fulldata", jsonObject.toString())
                // part of the "data" JSON file, contains request date, status, statistics
                val rData = jsonObject.getJSONObject("data")
                Log.d("dates", "$rData")
                // part "status" of the block "date" JSON file
                status = rData.getJSONObject("attributes").getString("status")
                // vendor score statistics, there are 4 categories: harmless, malicious, suspicious, undetected
                val stats = rData.getJSONObject("attributes").getJSONObject("stats")
                Log.d("stats", stats.toString())

                val t1 = findViewById<TextView>(R.id.t1)
                val tvStatus = findViewById<TextView>(R.id.tvStatus)

                runOnUiThread {
                    tvStatus.text = getString(R.string.message_status, status)
                    t1.text = getString(R.string.message_result, stats.getString("harmless"),
                        stats.getString("malicious"), stats.getString("undetected"), "\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("error", "$e")
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