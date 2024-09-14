package com.example.tripadvisor

import DBHelper
import RetrofitInstance
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var tvUserName: TextView
    private lateinit var tvTotalUser: TextView
    private lateinit var empty: ImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var reviewadapter: reviewAdapter
    private lateinit var view: View
    private lateinit var db: DBHelper
    private var receivedId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        // Check if user is logged in
        val user = firebaseAuth.currentUser

        if (user == null) {
            // Redirect to sign-in activity
            receivedId = arguments?.getInt("ID", -1) ?: -1
            var intent=Intent(activity, SignIn::class.java)
            intent.putExtra("ID", receivedId)
            startActivity(intent)
            activity?.finish()
        } else {

           view = inflater.inflate(R.layout.fragment_review, container, false)
            // Get UI elements
            tvUserName = view.findViewById(R.id.tv_user_name)
            recyclerView = view.findViewById(R.id.rl_recent_review)
            addButton = view.findViewById(R.id.addbtn)
            tvTotalUser = view.findViewById(R.id.tv_total_adventure)
            empty = view.findViewById(R.id.iv_empty)
            db = DBHelper(requireContext())

            val email = user.email
            val name = email?.substringBefore("@") ?: "User"
            tvUserName.text = name

            //retireve data from sqlite for feed items
            var advLst=db.getUserAdventuresListById(db.getUserAdventures(email))
            var size=advLst.size
            tvTotalUser.text= "${size.toString()} adventure"

            if(size==0)
            {
                empty.visibility = View.VISIBLE
            }
            else
            {
                empty.visibility= View.GONE
            }

            // Set up RecyclerView (you need to define your adapter)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            reviewadapter = reviewAdapter(requireContext(),advLst) // Replace with your adapter implementation
            recyclerView.adapter = reviewadapter

            reviewadapter.onItemClick6 = { Id ->
                val intent = Intent(context, AdventureDetails::class.java)
                intent.putExtra("adventureId", Id)
                startActivity(intent)
            }

            //delete adevnture from server and SQlite DB
            reviewadapter.onItemClick7 = { id ->

                GlobalScope.launch {
                    try {

                        val response=RetrofitInstance.api.deleteAdventure(id)

                        withContext(Dispatchers.Main) {

                            if (response.isSuccessful && response.code() == 204) {
                                // Adventure deleted successfully in SQlite
                                db.deleteAdventure(id)//From SQLite
                                db.deleteUserAdventure(id)//From SQLite

                                Toast.makeText(context, "Succesfully deleted adventure :${id}", Toast.LENGTH_LONG).show()
                            }
                            else {

                                Toast.makeText(context, "Failed to Delete Adventure on server: ${id}", Toast.LENGTH_LONG).show()
                            }
                        }

                    }catch (e: Exception){
                        Toast.makeText(context, "Failed to Delete Adventure: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Handle "Add a Missing Adventure" button click
        addButton.setOnClickListener {
            val intent = Intent(activity, AddMissingPlaceActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
