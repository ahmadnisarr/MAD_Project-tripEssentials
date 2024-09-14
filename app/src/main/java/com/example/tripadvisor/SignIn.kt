package com.example.tripadvisor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private var id:Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textView : TextView = findViewById(R.id.textView)
        val button : Button = findViewById(R.id.button)
        val emailEt : EditText = findViewById(R.id.emailEt)
        val passET : EditText = findViewById(R.id.passET)

        id = intent.getIntExtra("ID", -1)
        Log.d("SignInActivity", "Received ID: $id")


        firebaseAuth = FirebaseAuth.getInstance()
        textView.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val email = emailEt.text.toString()
            val pass = passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("ID", id)  // Pass ID here
                        startActivity(intent)
                        finish()  // Optional: finish the SignIn activity if not needed anymore
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun receiveDataFromFragment(receivedId: Int) {

        id=receivedId
        Log.d("Sig In Activity", "Data received: $id")
    }
}