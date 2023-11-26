package com.example.qrapp

import android.content.Intent
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
    private var apiKeyVT = "330f3e566b7dd0b52a13b342bab463159473559684c3a2f4559f4441371df88e"
    val apiKeyKasp = "pQe+E0W8RhuJfGppBaRyzg=="
    private var url = "https://www.virustotal.com/api/v3/urls"

    private var client = OkHttpClient()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_result)

        var checkURL = intent.getStringExtra("${R.string.key_value}")

        var textResult = findViewById<TextView>(R.id.tvResult)
        textResult.text = getString(R.string.received_URL, checkURL, "\n\n")

        val requestBody = FormBody.Builder()
            .add("url", checkURL.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("x-apikey", apiKeyVT)
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
                secondRequestVT(finalURL)

                //парсим каспера
                requestKasperTI(checkURL!!)
            }

            catch (e: IOException)
            {
                Log.d("error", "$e")
                e.printStackTrace()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun secondRequestVT(url2: String) {

        val request = Request.Builder()
            .url(url2)
            .addHeader("x-apikey", apiKeyVT)
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

    private fun requestKasperTI(url: String){
        val urlKasp = "https://opentip.kaspersky.com/api/v1/search/url?request=$url"

        val request = Request.Builder()
            .url(urlKasp)
            .addHeader("x-api-key", apiKeyKasp)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("loge", "error $e")
                println("Error occurred: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        Log.d("loge", responseBody)
                        val t2 = findViewById<TextView>(R.id.t2)
                        val status2 = findViewById<TextView>(R.id.tvStatus2)
                        runOnUiThread {
                            if ("Green" in responseBody) {t2.text = getString(R.string.no_harmless)}
                            else if (("Red" in responseBody) or ("Yellow" in responseBody)) {t2.text = getString(R.string.harmless)}
                            else if ("Grey" in responseBody) {t2.text = getString(R.string.no_info)}
                            status2.setText("Статус запроса: Выполнено")
                        }
                    }
                }
                else {
                    Log.d("loge", "error ${response.code}")
                }
            }
        })

    }

    // identify toolbar markup
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.icon_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {    // if the home button is pressed,  go back to MainActivity
                val intent = Intent(this, MainActivity ::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}