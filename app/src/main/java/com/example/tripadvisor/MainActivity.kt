package com.example.tripadvisor

import DBHelper
import RetrofitInstance
import kotlinx.coroutines.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tripadvisor.R.id
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private val PREFS_NAME = "MyPrefs"
    private val KEY_DATA_FETCHED = "data_fetched"
    private val KEY_LAST_FETCH_TIME = "last_fetch_time"
    private val SESSION_TIMEOUT_MS = 60 * 60 * 1000 // 1 hour in milliseconds
    private var Id:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Id = intent.getIntExtra("ID", -1)
        Log.e("TAG", " Id: $Id")
        if (Id == 1) {

            replaceFragment(AccountFragment())
            true
        }
        else if(Id==2) {
            Log.e("RepalceFragment", " Id: $Id")
            replaceFragment(ReviewFragment())
            true
        }
        else if(Id==3)
        {
            startActivity(Intent(this, AddMissingPlaceActivity::class.java))
        }





        db = DBHelper(this)

        // Retrieve SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isDataFetched = sharedPreferences.getBoolean(KEY_DATA_FETCHED, false)
        val lastFetchTime = sharedPreferences.getLong(KEY_LAST_FETCH_TIME, 0L)
        val currentTime = System.currentTimeMillis()

        // Check if the session has expired
        if (!isDataFetched || (currentTime - lastFetchTime > SESSION_TIMEOUT_MS)) {
            // Retrieve Data from Server and store it in SQLite
            val coroutineScope = CoroutineScope(Dispatchers.IO)
            coroutineScope.launch {
                try {

                    fetchAndStoreData()

                    // Mark data as fetched and update the last fetch time
                    with(sharedPreferences.edit()) {
                        putBoolean(KEY_DATA_FETCHED, true)
                        putLong(KEY_LAST_FETCH_TIME, currentTime)
                        apply()
                    }

                } catch (e: Exception) {
                    Log.e("MainActivity", "Error saving data into DB SQLite", e)
                }
            }
        }

        // Set default fragment
        replaceFragment(ExploreFragment())

        // Setup BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_explore -> {
                    val exploreFragment = ExploreFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ID", 3)
                        }
                    } // Set the bundle as fragment arguments
                    replaceFragment(exploreFragment)
                    true
                }
                R.id.navigation_search -> {

                    replaceFragment(SearchFragment())
                    true
                }
                R.id.navigation_review -> {
                    val reviewFragment = ReviewFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ID", 2)
                        }
                    } // Set the bundle as fragment arguments
                    replaceFragment(reviewFragment)
                    true
                }
                R.id.navigation_account -> {
                    val accountFragment = AccountFragment().apply {
                        arguments = Bundle().apply {
                            putInt("ID", 1)
                        }
                    } // Set the bundle as fragment arguments
                    replaceFragment(accountFragment)
                    true
                }
                else -> false
            }
        }
    }

    private suspend fun fetchAndStoreData() = coroutineScope {
        try {
            db.addCountries((RetrofitInstance.api.GetAllCountry()))
            delay(500)
            db.addAdventures(RetrofitInstance.api.GetAllAdventure())
            delay(500) // Delay before next call

            db.addResturants(RetrofitInstance.api.GetAllResturant())
            delay(500) // Delay before next call

            db.addHotels(RetrofitInstance.api.GetAllHotels())
            delay(500) // Delay before next call

            db.addHotelDetails(RetrofitInstance.api.GetAllHotelDetails())
            delay(500) // Delay before next call
            db.addResturantDetails(RetrofitInstance.api.GetAllResturantDetails())
        delay(500)
            Log.e("MainActivity", "All data Feteched")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error during API call", e)
            throw e
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
