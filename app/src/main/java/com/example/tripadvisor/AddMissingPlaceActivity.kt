package com.example.tripadvisor
import DBHelper
import com.example.tripadvisor.R
import com.example.tripadvisor.adventures

import RetrofitInstance.api
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddMissingPlaceActivity : AppCompatActivity() {

    private lateinit var ivSelectedImage: ImageView
    private var imageFileName: String? = null
    private lateinit var db: DBHelper
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        // Check if user is authenticated
        if (user == null) {
            // User is not signed in, redirect to SignIn activity
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("ID", 3)
            startActivity(intent)
            finish() // Close the current activity
            return
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_missing_place)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val etName = findViewById<EditText>(R.id.name)
        val etPrice = findViewById<EditText>(R.id.price)
        val etAbout = findViewById<EditText>(R.id.about)
        val etHighlights = findViewById<EditText>(R.id.hightlight)
        val btnUploadImage = findViewById<Button>(R.id.btnUploadImage)
        val btnSubmit = findViewById<Button>(R.id.submitButton)
        ivSelectedImage = findViewById(R.id.ivSelectedImage)

        btnUploadImage.setOnClickListener {
            openImagePicker()
        }

        btnSubmit.setOnClickListener {
            val name = etName.text.toString()
            val price = etPrice.text.toString().toDoubleOrNull()
            val about = etAbout.text.toString()
            val highlights = etHighlights.text.toString()
            db = DBHelper(this)

            if (name.isNotEmpty() && price != null && about.isNotEmpty() && highlights.isNotEmpty() && imageFileName != null) {
                var adventure = adventures(id=0,name=name, price=price, image=imageFileName!!, about=about, hightlights = highlights)
                // Handle form submission, e.g., save to database or send to server

                //api call for data datasaved in database
                // Launch a coroutine to make the API call
                GlobalScope.launch {
                    try
                    {
                        Log.d("Before AddMissingPlaceActivity Api call ", "${adventure}")
                        Log.d("AddMissingPlaceActivity", "Starting API call")
                        val response = api.addAdventure(adventure)

                        if (response.isSuccessful) {
                            // Response is successful, extract the ID
                            val newId = response.body() ?: throw Exception("No ID received")
                            adventure.id = newId

                            val newAdd=api.GetAdventureById(newId)
                            db.addAdventure(newAdd)

                            val adv:userAdventures=userAdventures(
                                userID= user.email ,
                                adventureID=newId
                            )

                            db.addUserAdventure(adv)


                            Log.d("AddMissingPlaceActivity", "API call successful. New ID: $newId")
                        } else {
                            // Handle unsuccessful response
                            Log.e("AddMissingPlaceActivity", "API call failed with status: ${response.code()}")
                        }
                        Log.d("AddMissingPlaceActivity", "API call successful:")

                        // Handle the success on the main thread
                        withContext(Dispatchers.Main) {
                            // Show a success message or navigate to another screen
                            Toast.makeText(this@AddMissingPlaceActivity, "Adventure added successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // Handle the error on the main thread
                        withContext(Dispatchers.Main) {
                            // Show an error message
                            Toast.makeText(this@AddMissingPlaceActivity, "Failed to add adventure: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else {
                // Show an error message if fields are missing
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "Adventure Added", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            data?.data?.let { uri ->

                Log.e("1. Image URI", "Image file at $uri")
                ivSelectedImage.visibility = ImageView.VISIBLE

                try {
                    //display the image
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    ivSelectedImage.setImageBitmap(bitmap)

                    imageFileName = encodeImageToBase64(bitmap)

                } catch (e: Exception) {
                    Log.e("ImageProcessingError", "Error processing image: ${e.message}")
                }
            }
        }
    }

    fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }


//    private fun saveImageToInternalStorage(bitmap: Bitmap): String? {
//        val directory = File(filesDir, "imagesFolder")
//        if (!directory.exists()) {
//            directory.mkdir()
//        }
//        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
//
//        val file = File(directory, fileName)
//
//        try {
//            FileOutputStream(file).use { out ->
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
//            }
//            return fileName
//        } catch (e: IOException) {
//            e.printStackTrace()
//            return null
//        }
//    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}