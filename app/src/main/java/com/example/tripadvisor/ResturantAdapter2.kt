package com.example.tripadvisor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import java.net.URLEncoder

class ResturantAdapter2(private val context: Context, private val hotelList: List<resturant>) :
    RecyclerView.Adapter<ResturantAdapter2.FoodViewHolder>()
{
    var onItemClick4 : ((Int)->Unit) ? = null

    // ViewHolder class for RecyclerView items
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val HotelImage: ImageView = itemView.findViewById(R.id.imgHotel)
        val HotelName: TextView = itemView.findViewById(R.id.tvHotelName)
        val HotelRating: TextView = itemView.findViewById(R.id.tvHotelRating)
        val HotelAddress: TextView = itemView.findViewById(R.id.tvHotelAddress)
        val HotelLink: TextView = itemView.findViewById(R.id.tvLink)
        val HotelPrice: TextView = itemView.findViewById(R.id.price)
        val HotelViewBtn: Button = itemView.findViewById(R.id.btnViewDeal)

    }

    // Inflate item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return FoodViewHolder(view)
    }

    // Return the size of the country list
    override fun getItemCount(): Int {
        return hotelList.size
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val hotel = hotelList[position]

        val path=hotel.images

        Glide.with(context)
            .load(RetrofitInstance.BASE_URL+path)
            .into(holder.HotelImage);


        //Decode image and display it
//        holder.HotelImage.setImageBitmap(decodeBase64ToBitmap(hotel.images))


        holder.HotelName.text = hotel.name
//        holder.HotelLink.text=hotel.officalSite
        holder.HotelLink.text = "Visit the Website"
        holder.HotelLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(hotel.menu)
            holder.itemView.context.startActivity(intent)
        }
//        holder.HotelPrice.text = "$" + hotel.price.toString()
        val baseUrl = "https://www.booking.com/searchresults.en-gb.html"
        val hotelName =hotel.name // Replace with the actual hotel name
        val encodedHotelName = URLEncoder.encode(hotelName, "UTF-8")
        val url = "$baseUrl?aid=7344586&label=metatripad-link-mmetain-hotel-50834_xqdz-b5d8030dbd95e45d35bbdfb0ca681ed8_los-01_bw-000_tod-0_dom-couk_curr-GBP_gst-04_nrm-01_clkid-ab00d301-f6f9-4a8b-871f-3981acbbce67_aud-0000_mbl-M_pd-T_sc-2_defdate-0_spo-0_clksrc-0_mcid-10_mobsrc-1_appsrc-1&utm_medium=mmeta&no_rooms=1&show_room=5083421_200932029_0_2_0_359779&utm_content=los-01_bw-000_dom-couk_defdate-0_spo-0_clksrc-0_mobsrc-1_appsrc-1&utm_campaign=in&utm_term=$encodedHotelName&utm_source=metatripad&highlighted_hotels=50834&checkin=2024-08-28&redirected=1&city=-1456928&hlrd=with_dates&group_adults=4&source=hotel&group_children=0&checkout=2024-08-29&keep_landing=1&sid=977e1ad056cf2ddf556dcdb0428dfb3f"



        // Create the intent to open the URL
        holder.HotelViewBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {

            onItemClick4?.invoke(hotel.id)
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

