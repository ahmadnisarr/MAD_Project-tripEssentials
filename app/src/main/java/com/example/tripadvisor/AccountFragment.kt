package com.example.tripadvisor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var receivedId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        val signOutButton = view.findViewById<Button>(R.id.sign_out_button)
        val signInButton = view.findViewById<Button>(R.id.sign_in_button)
        val profileSection = view.findViewById<LinearLayout>(R.id.profile_section)
        val changePasswordOption = view.findViewById<LinearLayout>(R.id.change_password_option)

        // Check if the user is authenticated
        val user = firebaseAuth.currentUser
        receivedId = arguments?.getInt("ID", -1) ?: -1

        if (user != null) {
            // User is signed in, show the sign-out button and other sections
            profileSection.visibility = View.VISIBLE
            changePasswordOption.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            signInButton.visibility = View.GONE
        } else {
            // User is not signed in, hide the sign-out button and sections
            profileSection.visibility = View.GONE
            changePasswordOption.visibility = View.GONE
            signOutButton.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
        }

        // Set up the sign-out button click listener
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()

            // Update UI after sign-out
            profileSection.visibility = View.GONE
            changePasswordOption.visibility = View.GONE
            signOutButton.visibility = View.GONE
            signInButton.visibility = View.VISIBLE
        }

        // Set up the sign-in button click listener
        signInButton.setOnClickListener {
            val intent = Intent(requireContext(), SignIn::class.java)
            intent.putExtra("ID", receivedId)
            startActivity(intent)
        }

        // Set up the Change Password option click listener
        changePasswordOption.setOnClickListener {
            val intent = Intent(requireContext(), passwordChange::class.java)
            startActivity(intent)
        }

        return view
    }
}
