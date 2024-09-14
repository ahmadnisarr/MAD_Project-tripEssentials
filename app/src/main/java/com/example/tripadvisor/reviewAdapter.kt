package com.example.tripadvisor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class reviewAdapter(
    private val context: Context,
    private val adventureList: List<adventures>
) : RecyclerView.Adapter<reviewAdapter.FoodViewHolder>() {

    var onItemClick6: ((Int) -> Unit)? = null
    var onItemClick7: ((Int) -> Unit)? = null

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adventureImage: ImageView = itemView.findViewById(R.id.iv_review_image)
        val name: TextView = itemView.findViewById(R.id.tv_review_name)
        val price: TextView = itemView.findViewById(R.id.tv_review_price)
        val deletebtn: Button = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_items, parent, false)
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
        holder.name.text = adventure.name
//        holder.adventureName2.text = "Full-day Tour"
        holder.price.text = "From $${adventure.price} per adult"

        // Handle item clicks
        holder.itemView.setOnClickListener {
            onItemClick6?.invoke(adventure.id)
        }

        holder.deletebtn.setOnClickListener({
            onItemClick7?.invoke(adventure.id)
        })


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
