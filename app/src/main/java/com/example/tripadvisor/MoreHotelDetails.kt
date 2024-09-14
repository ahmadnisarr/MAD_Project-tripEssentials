package com.example.tripadvisor

import DBHelper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.URLEncoder

class MoreHotelDetails : AppCompatActivity() {

    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_more_hotel_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }


    private fun init() {
        db = DBHelper(this)

        val Id = intent.getIntExtra("Id", -1)

        if (Id != -1) {
            GlobalScope.launch {
                try {
                    Log.d("MoreHotelDetails", "Starting API call ID:${Id}")

                    val data=  db.getHotelDetailsByHotelId(Id)
//                        .getMoreHotelDetails(Id)
                    val datahotel= db.getHotelById(Id)
//                        .getByIdHotel(Id)

                    Log.d("MoreHotelDetails", "API call successful: ${data} MoreHotelDetails fetched")


                    // Bind the hotel data to the views on the main thread
                    withContext(Dispatchers.Main) {
                        bindHotelData(datahotel,data)
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

    private fun bindHotelData(datahotel:hotel,hotel: hotelDetails) {
        val imgHotel: ImageView = findViewById(R.id.imgHotel)


        val path=datahotel.images

        Glide.with(this)
            .load(RetrofitInstance.BASE_URL+path)
            .into(imgHotel);


//        imgHotel.setImageBitmap(decodeBase64ToBitmap(datahotel.images))

//        val imgHotel: ImageView = findViewById(R.id.imgHotel)
//        val firstImageName = datahotel.images.split(",")[0].trim()
//        val imageResId = this.resources.getIdentifier(
//            firstImageName, "drawable", this.packageName
//        )
//        imgHotel.setImageResource(imageResId)

        // Set the hotel name
        findViewById<TextView>(R.id.tvHotelName).text =datahotel.name
        // Set the website link
        findViewById<TextView>(R.id.websiteLink).text = hotel.websiteLink
        findViewById<TextView>(R.id.websiteLink).setOnClickListener {
            // Handle the click to open the website
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(hotel.websiteLink))
            startActivity(intent)
        }

        // Set the call link
        findViewById<TextView>(R.id.callLink).text = hotel.phoneNumber
        findViewById<TextView>(R.id.callLink).setOnClickListener {
            // Handle the click to make a phone call
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hotel.phoneNumber}"))
            startActivity(intent)
        }

        // Set the email link
        findViewById<TextView>(R.id.emailLink).text = hotel.email
        findViewById<TextView>(R.id.emailLink).setOnClickListener {
            // Handle the click to send an email
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${hotel.email}"))
            startActivity(intent)
        }
        val bookingbtn=findViewById<Button>(R.id.btnViewDeal)

        val baseUrl = "https://www.booking.com/searchresults.en-gb.html"
        val hotelName =datahotel.name// Replace with the actual hotel name
        val encodedHotelName = URLEncoder.encode(hotelName, "UTF-8")

        val url = "$baseUrl?aid=7344586&label=metatripad-link-mmetain-hotel-50834_xqdz-b5d8030dbd95e45d35bbdfb0ca681ed8_los-01_bw-000_tod-0_dom-couk_curr-GBP_gst-04_nrm-01_clkid-ab00d301-f6f9-4a8b-871f-3981acbbce67_aud-0000_mbl-M_pd-T_sc-2_defdate-0_spo-0_clksrc-0_mcid-10_mobsrc-1_appsrc-1&utm_medium=mmeta&no_rooms=1&show_room=5083421_200932029_0_2_0_359779&utm_content=los-01_bw-000_dom-couk_defdate-0_spo-0_clksrc-0_mobsrc-1_appsrc-1&utm_campaign=in&utm_term=$encodedHotelName&utm_source=metatripad&highlighted_hotels=50834&checkin=2024-08-28&redirected=1&city=-1456928&hlrd=with_dates&group_adults=4&source=hotel&group_children=0&checkout=2024-08-29&keep_landing=1&sid=977e1ad056cf2ddf556dcdb0428dfb3f"

        // Create the intent to open the URL
        bookingbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
           this.startActivity(intent)
        }

//        val url="https://www.booking.com/searchresults.en-gb.html?aid=7344586&label=metatripad-link-mmetain-hotel-50834_xqdz-b5d8030dbd95e45d35bbdfb0ca681ed8_los-01_bw-000_tod-0_dom-couk_curr-GBP_gst-04_nrm-01_clkid-ab00d301-f6f9-4a8b-871f-3981acbbce67_aud-0000_mbl-M_pd-T_sc-2_defdate-0_spo-0_clksrc-0_mcid-10_mobsrc-1_appsrc-1&utm_medium=mmeta&no_rooms=1&show_room=5083421_200932029_0_2_0_359779&utm_content=los-01_bw-000_dom-couk_defdate-0_spo-0_clksrc-0_mobsrc-1_appsrc-1&utm_campaign=in&utm_term=hotel-50834&utm_source=metatripad&highlighted_hotels=50834&checkin=2024-08-28&redirected=1&city=-1456928&hlrd=with_dates&group_adults=4&source=hotel&group_children=0&checkout=2024-08-29&keep_landing=1&sid=977e1ad056cf2ddf556dcdb0428dfb3f"
//        bookingbtn.setOnClickListener{
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(url)
//            this.startActivity(intent)
//        }
        val webbtn=findViewById<TextView>(R.id.websiteLink)
        webbtn.text = "Visit the Website"
        webbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(datahotel.officalSite)
            this.startActivity(intent)
        }
        val callbtn = findViewById<TextView>(R.id.callLink)
        callbtn.text = "Call"

        callbtn.setOnClickListener {
            val phoneNumber = hotel.phoneNumber // Assume `datahotel` has the phone number
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        }

        val emailbtn = findViewById<TextView>(R.id.emailLink)
        emailbtn.text = "Email"

        emailbtn.setOnClickListener {
            val emailAddress = hotel.email // Assume `hotel` has the email address
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$emailAddress"))
            startActivity(intent)
        }

        // Set the about section
        findViewById<TextView>(R.id.About).text = hotel.about

        // Set the address
        findViewById<TextView>(R.id.address).text = hotel.address

        // Other fields such as amenities, price, and booking details should be similarly set
        // Ensure you have appropriate data in your `HotelDetails` class or add them as needed
    }

//    private fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
//        return when {
//            base64Str.isNullOrEmpty() -> {
//                Log.e("AdventureDetails", "Base64 string is null or empty")
//                null
//            }
//            else -> try {
//                val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
//                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//            } catch (e: IllegalArgumentException) {
//                Log.e("AdventureDetails", "Failed to decode Base64 image", e)
//                null
//            }
//        }
//    }
}