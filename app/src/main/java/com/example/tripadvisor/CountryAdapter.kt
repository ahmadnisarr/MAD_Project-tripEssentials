package com.example.tripadvisor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException



class CountryAdapter(private val context: Context,private val countryList: List<countries>,) :
    RecyclerView.Adapter<CountryAdapter.FoodViewHolder>()
{
       var onItemClick : ((Int)->Unit) ? = null
    // in this class we have to find all widget id from layout which we select
    // for recycler view
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryImage: ImageView = itemView.findViewById(R.id.countryImage)
        val countryName: TextView = itemView.findViewById(R.id.countryText)
    }


    // in this method we have to return layout view like item layout in our project
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_hotel,parent,false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }


    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val country = countryList[position]
        val path=country.imagePath.split(',')[0];

        Glide.with(context)
            .load(RetrofitInstance.BASE_URL+path)
            .into(holder.countryImage);


        //holder.countryImage.setImageBitmap(decodeBase64ToBitmap( country.imagePath))

//        // Get resource ID from the image path string (assuming it's the resource name)
//        val imageResId = holder.itemView.context.resources.getIdentifier(
//            country.imagePath, "drawable", holder.itemView.context.packageName
//        )
//
//        holder.countryImage.setImageResource(imageResId)
        holder.countryName.text = country.name



        holder.itemView.setOnClickListener {
            onItemClick?.invoke(country.id)
        }

    }



//    private fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
//        return when {
//            base64Str.isNullOrEmpty() -> {
//                Log.e("AdventureDetails", "Base64 string is null or empty")
//                null
//            }
//
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