package com.example.tripadvisor

import DBHelper
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchFragment : Fragment() {

    private lateinit var db: DBHelper
    private lateinit var searchBox: EditText
    private lateinit var hotelRecyclerView: RecyclerView
    private lateinit var restaurantRecyclerView: RecyclerView
    private lateinit var hotelAdapter: HotelAdapter
    private lateinit var restaurantAdapter2: ResturantAdapter2
    private lateinit var emptyImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        db = DBHelper(requireContext())
        searchBox = view.findViewById(R.id.search_box)
        hotelRecyclerView = view.findViewById(R.id.hotelRecycleView)
        restaurantRecyclerView = view.findViewById(R.id.resturantRecycleView)
        emptyImageView = view.findViewById(R.id.iv_empty)

        hotelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        restaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val searchID = arguments?.getInt("searchID")

        when (searchID) {
            11 -> {
                searchBox.hint = "Search Hotel"
                hotelRecyclerView.visibility = View.VISIBLE
                restaurantRecyclerView.visibility = View.GONE
            }
            else ->{
                searchBox.hint = "Search Restaurant"
                restaurantRecyclerView.visibility = View.VISIBLE
                hotelRecyclerView.visibility = View.GONE
            }
        }


        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    searchInDatabase(query, searchID)
                } else {
                    // Clear results if the search query is empty
                    hotelRecyclerView.adapter = null
                    restaurantRecyclerView.adapter = null
                    emptyImageView.visibility = View.VISIBLE
                }
            }
        })


//        searchBox.setOnEditorActionListener { _, _, _ ->
//            val query = searchBox.text.toString()
//            if (query.isNotEmpty()) {
//                Toast.makeText(requireContext(), "search: ${query}", Toast.LENGTH_SHORT).show()
//                searchInDatabase(query, searchID)
//            } else {
//                Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
//            }
//            true
//        }

        return view
    }

    private fun searchInDatabase(query: String, searchID: Int?) {
        val results = when (searchID) {
            11 -> db.searchHotel(query) as? List<hotel> ?: emptyList()
            else -> db.searchResturant(query) as? List<resturant> ?: emptyList()
        }

        if (results.isNotEmpty()) {
            emptyImageView.visibility = View.GONE


            if (searchID == 11) {
                val hotelResults = results as List<hotel>
                hotelAdapter = HotelAdapter(requireContext(),hotelResults) // Pass the results to the adapter
                hotelRecyclerView.adapter = hotelAdapter
                hotelRecyclerView.visibility = View.VISIBLE
                restaurantRecyclerView.visibility = View.GONE
            } else {
                val restaurantResults = results as List<resturant>
                restaurantAdapter2 = ResturantAdapter2(requireContext(),restaurantResults) // Pass the results to the adapter
                restaurantRecyclerView.adapter = restaurantAdapter2
                restaurantRecyclerView.visibility = View.VISIBLE
                hotelRecyclerView.visibility = View.GONE
            }

        } else {
            emptyImageView.visibility = View.VISIBLE
            hotelRecyclerView.visibility = View.GONE
            restaurantRecyclerView.visibility = View.GONE
            Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
        }
    }
}
