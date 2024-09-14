package com.example.tripadvisor


import DBHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExploreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var adventureAdapter: AdventureAdapter
    private lateinit var resturantAdapter: ResturantAdapter
    private lateinit var db: DBHelper
    private var receivedId: Int? = null

    private lateinit var rest: List<resturant>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragmnet_explore, container, false)

        //Grid layout
        arguments?.let {
            receivedId = it.getInt("ID", -1)  // Default value is -1 if not found
        }

        val hotelbtn=view.findViewById<Button>(R.id.hotelExplore)
        val resturantbtn=view.findViewById<Button>(R.id.restruantExplore)
        val keepExplore=view.findViewById<Button>(R.id.keepExplore)

        hotelbtn.setOnClickListener {

            navigateToSearchFragment(11)
        }
        resturantbtn.setOnClickListener {
            navigateToSearchFragment(12)
        }
        keepExplore.setOnClickListener {
            startActivity(Intent(context,webView::class.java))
        }

        db = DBHelper(requireContext())

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.hotelOffer)
        recyclerView2 = view.findViewById(R.id.adventure)
        recyclerView3 = view.findViewById(R.id.foodOffer)
        recyclerView.setHasFixedSize(true)
        recyclerView2.setHasFixedSize(true)
        recyclerView3.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerView3.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        GlobalScope.launch {
            try {



                val country = db.getAllCountry()
                val adventure =db.getAllAdventure()
                val resturant = db.getAllResturant()



                rest = country.map { it ->
                    resturant(
                        id = it.id,  // Replace with actual restaurant ID if available
                        name = it.name,  // Replace with actual restaurant name if available
                        images = it.imagePath,  // Replace with actual restaurant image URL if available
                        countryId = -1,
                        menu = ""
                    )
                }
                // Update the ListView on the UI thread
                withContext(Dispatchers.Main) {
                    countryAdapter = CountryAdapter(requireContext(),country)
                    adventureAdapter = AdventureAdapter(requireContext(), adventure) // Use requireContext() here
                    resturantAdapter = ResturantAdapter(requireContext(),country)
                    recyclerView.adapter = countryAdapter
                    recyclerView2.adapter = adventureAdapter
                    recyclerView3.adapter = resturantAdapter


                    countryAdapter.onItemClick = { countryId ->
                        val intent = Intent(context, HotelDetail::class.java)
                        intent.putExtra("countryId", countryId)
                        startActivity(intent)
                    }

                    resturantAdapter.onItemClick3 = { countryId ->
                        val intent = Intent(context, ResturantDetail::class.java)
                        intent.putExtra("resturantId", countryId)
                        startActivity(intent)
                    }


                    adventureAdapter.onItemClick2 = { id ->
                        val intent = Intent(context, AdventureDetails::class.java)
                        intent.putExtra("adventureId", id)
                        startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                Log.e("viewAllStudent", "Error during API call", e)
                withContext(Dispatchers.Main) {
                    // Display an error message or take another appropriate action
                    Toast.makeText(context, "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
                    // You can also set adapters to empty lists to clear the RecyclerViews
                    countryAdapter = CountryAdapter(requireContext(),emptyList())
                    adventureAdapter = AdventureAdapter(requireContext(),emptyList())
                    recyclerView.adapter = countryAdapter
                    recyclerView2.adapter = adventureAdapter
                }
            }
        }

        val addForm = view.findViewById<Button>(R.id.addbtn)
        addForm.setOnClickListener {
            val intent = Intent(context, AddMissingPlaceActivity::class.java)
            startActivity(intent)
        }

        return view
    }
    private fun navigateToSearchFragment(searchID: Int) {
        val searchFragment = SearchFragment()

        val bundle = Bundle()
        bundle.putInt("searchID", searchID) // Pass the searchID to the fragment

        // Set the arguments for the fragment
        searchFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchFragment)
            .addToBackStack(null)
            .commit()
    }
}
