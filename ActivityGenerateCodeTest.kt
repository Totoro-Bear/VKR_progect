package com.example.qrapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class ActivityGenerateCodeTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_code_test)

        // correlating a variable and a markup object on the screen
        val button = findViewById<Button>(R.id.button_for_generate2)
        val imageView = findViewById<ImageView>(R.id.QRcode)
        val editText = findViewById<EditText>(R.id.fieldInputDate)



        // when pressed, a Qr code is generated
        button.setOnClickListener {

            if (editText.text.toString() == "") {
                editText.setHintTextColor(Color.RED)
            }

            else {

                val multiFormatWriter = MultiFormatWriter()  // for create binary matrix
                val barcodeEncoder = BarcodeEncoder()  //for converts binary matrix to a bitmap image

                try {
                    val bitMatrix = multiFormatWriter.encode(editText.text.toString(), BarcodeFormat.QR_CODE, 600, 600)  // binary matrix
                    val bitmapPicture = barcodeEncoder.createBitmap(bitMatrix)
                    imageView.setImageBitmap(bitmapPicture) // output picture
                    imageView.visibility = View.VISIBLE
                } catch (e: WriterException) {  // if data cannot be encoded
                    throw RuntimeException(e)
                }
            }

        }
}
}