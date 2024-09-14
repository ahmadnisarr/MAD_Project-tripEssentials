package com.example.tripadvisor

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class HotelDetail : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var hotelAdapter: HotelAdapter
    private lateinit var db: DBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hotel_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    private fun init() {
        db = DBHelper(this)
        recyclerView = findViewById(R.id.hotelRecycleView)
        recyclerView.setHasFixedSize(true)
        // how to manage recycler view we used layout manager
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val countryId = intent.getIntExtra("countryId", -1)

        if (countryId != -1)
        {

            GlobalScope.launch {
                try {
                    Log.d("HotelDetails", "Starting API call ID:${countryId}")

                    val hotels = db.getHotelByCountryId(countryId)

                    Log.d("HotelDetails", "API call successful: ${hotels.size} Hotel fetched")
                    // Handle the list of hotels (e.g., update UI)

                    withContext(Dispatchers.Main) {

                        recyclerView.setHasFixedSize(true)
                        hotelAdapter = HotelAdapter(this@HotelDetail,hotels)
                        recyclerView.adapter = hotelAdapter


                        hotelAdapter.onItemClick2 = { id ->
                            Log.d("HotelDetail", "Starting MoreHotelDetails with ID: $id")
                            val intent = Intent(this@HotelDetail, MoreHotelDetails::class.java)
                            intent.putExtra("Id", id)
                            startActivity(intent)
                        }
                    }




                } catch (e: HttpException) {
                    // Handle HTTP errors
                } catch (e: Throwable) {
                    // Handle other errors
                }
            }



        } else {
            Toast.makeText(this, "Invalid country ID", Toast.LENGTH_SHORT).show()
        }

        // now initialize our FoodAdapter class and passing foodlist in it

    }







}