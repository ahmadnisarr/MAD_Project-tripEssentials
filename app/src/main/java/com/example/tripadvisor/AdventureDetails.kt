package com.example.tripadvisor

import DBHelper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import android.util.Base64
import com.bumptech.glide.Glide

class AdventureDetails : AppCompatActivity() {

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adventure_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize and fetch the data
        init()
    }

    private fun init() {
        db = DBHelper(this)
        val adventureId = intent.getIntExtra("adventureId", -1)

        if (adventureId != -1) {
            GlobalScope.launch {
                try {
                    Log.d("AdventureDetails", "Starting API call ID:${adventureId}")


                    val adventure = db.getAdventureById(adventureId)
                    Log.d("AdventureDetails", "API call successful: ${adventure} adventure fetched")

                    // Bind the hotel data to the views on the main thread
                    runOnUiThread {
                        bindHotelData(adventure)
                    }

                } catch (e: HttpException) {
                    Log.e("AdventureDetails", "HTTP Exception: ${e.message}", e)
                    // Handle HTTP errors
                } catch (e: Throwable) {
                    Log.e("AdventureDetails", "Error: ${e.message}", e)
                    // Handle other errors
                }
            }
        } else {
            Toast.makeText(this, "Invalid adventure ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindHotelData(hotel: adventures) {
        // Set the hotel image
       val img= findViewById<ImageView>(R.id.imgHotel)
        val path=hotel.image

        Glide.with(this)
            .load(RetrofitInstance.BASE_URL+path)
            .into(img);



//        findViewById<ImageView>(R.id.imgHotel).setImageBitmap(decodeBase64ToBitmap(hotel.image))

        // Set the hotel details
        findViewById<TextView>(R.id.tvHotelName).text = hotel.name
        findViewById<TextView>(R.id.tvHotelRating).text = " ⭐⭐⭐⭐ ${100} reviews"
        findViewById<TextView>(R.id.about).text = hotel.about
        findViewById<TextView>(R.id.highlights).text = hotel.hightlights
        findViewById<TextView>(R.id.price).text = "From \n$${hotel.price}\nper adult"
    }

//    private fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
//        if (base64String.isNullOrEmpty()) {
//            Log.e("AdventureDetails", "Base64 string is null or empty")
//            return null
//        }
//
//        // Remove any newlines or carriage returns from the Base64 string
//        val cleanBase64String = base64String.replace("\\n", "").replace("\\r", "")
//
//        return try {
//            val decodedString = Base64.decode(cleanBase64String, Base64.DEFAULT)
//            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//        } catch (e: IllegalArgumentException) {
//            Log.e("AdventureDetails", "Failed to decode Base64 image: bad base-64", e)
//            null
//        } catch (e: Exception) {
//            Log.e("AdventureDetails", "Unexpected error during Base64 decoding", e)
//            null
//        }
//    }

}
