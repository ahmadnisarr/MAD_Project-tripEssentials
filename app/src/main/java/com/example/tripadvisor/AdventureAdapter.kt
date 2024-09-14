package com.example.tripadvisor

import RetrofitInstance
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdventureAdapter(
    private val context: Context,
    private val adventureList: List<adventures>
) : RecyclerView.Adapter<AdventureAdapter.FoodViewHolder>() {

    var onItemClick2: ((Int) -> Unit)? = null

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adventureImage: ImageView = itemView.findViewById(R.id.adventureImage)
        val adventureName1: TextView = itemView.findViewById(R.id.adventureText1)
        val adventureName2: TextView = itemView.findViewById(R.id.adventureText2)
        val adventureName3: TextView = itemView.findViewById(R.id.adventureText3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adventure, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return adventureList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val adventure = adventureList[position]

        val path=adventure.image

        Glide.with(context)
            .load(RetrofitInstance.BASE_URL +path)
            .into(holder.adventureImage);


        // Decode the Base64 string to a Bitmap and set it to the ImageView
//        holder.adventureImage.setImageBitmap(decodeBase64ToBitmap(adventure.image))

        // Set other text views
        holder.adventureName1.text = adventure.name
        holder.adventureName2.text = "Full-day Tour"
        holder.adventureName3.text = "From $${adventure.price} per adult"

        // Handle item clicks
        holder.itemView.setOnClickListener {
            onItemClick2?.invoke(adventure.id)
        }
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
