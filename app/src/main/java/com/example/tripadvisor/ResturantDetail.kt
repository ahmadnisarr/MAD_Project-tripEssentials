package com.example.tripadvisor

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException


class ResturantDetail : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resturantAdapter: ResturantAdapter2
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resturant_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }





    private fun init() {
        db = DBHelper(this)
        recyclerView = findViewById(R.id.resturantRecycleView)
        recyclerView.setHasFixedSize(true)
        // how to manage recycler view we used layout manager
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val countryId = intent.getIntExtra("resturantId", -1)

        if (countryId != -1)
        {

            GlobalScope.launch {
                try {
                    Log.d("ResturantsDetails", "Starting API call ID:${countryId}")

                    val rest = db.getResturantByCountryId(countryId)

                    Log.d("ResturantDetails", "API call successful: ${rest.size} Hotel fetched")
                    // Handle the list of hotels (e.g., update UI)
                    runOnUiThread {

                        recyclerView.setHasFixedSize(true)
                        resturantAdapter = ResturantAdapter2(this@ResturantDetail,rest)
                        recyclerView.adapter = resturantAdapter

                        resturantAdapter.onItemClick4 = { id ->
                            Log.d("HotelDetail", "Starting MoreHotelDetails with ID: $id")
                            val intent =
                                Intent(this@ResturantDetail, MoreResturantDetails::class.java)
                            intent.putExtra("Id", id)
                            startActivity(intent)
                        }
                    }




                } catch (e: HttpException) {
                    // Handle HTTP errors
                }
                catch (e: Throwable) {
                    // Handle other errors
                }
            }



        } else {
            Toast.makeText(this, "Invalid country ID", Toast.LENGTH_SHORT).show()
        }

    }


}