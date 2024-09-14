package com.example.tripadvisor
import android.telecom.Call
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface apiService {
    @GET("Country/GetAll")
    suspend fun GetAllCountry(): List<countries> // Note: Changed return type to suspend function for coroutines
    @GET("Adventure/GetAll")
    suspend fun GetAllAdventure(): List<adventures>

    @GET("Resturant/GetAll")
    suspend fun GetAllResturant(): List<resturant>

    @GET("Hotel/GetAll")
    suspend fun GetAllHotels():List<hotel>//1

    @GET("HotelDetail/GetAll")
    suspend fun GetAllHotelDetails():List<hotelDetails>//2

    @GET("ResturantDetail/GetAll")
    suspend fun GetAllResturantDetails():List<resturantDetails>//3

    @DELETE("Adventure/DeleteById")
    suspend fun deleteAdventure(@Query("id") id:Int): Response<Void>//4





    @GET("Resturant/GetResturantByCountryId")
    suspend fun getResturantDetails(@Query("countryId") countryId: Int): List<resturant>

    @GET("ResturantDetail/GetByResturantId")
    suspend fun getMoreResturantDetails(@Query("id") id: Int): resturantDetails

    @GET("Resturant/GeById")
    suspend fun getByIdResturant(@Query("Id") Id: Int): resturant



    @GET("Adventure/GetAdventureById")
    suspend fun GetAdventureById(@Query("Id") Id: Int): adventures

    @GET("Hotel/GeById")
    suspend fun getByIdHotel(@Query("Id") Id: Int): hotel

    @GET("Hotel/GetHotelByCountryId")
    suspend fun getCountryDetails(@Query("countryId") countryId: Int): List<hotel>

    @GET("HotelDetail/GetByhotelId")
    suspend fun getMoreHotelDetails(@Query("id") adventureId: Int): hotelDetails


    @GET("Hotel/GetHotelById")
    suspend fun getHotelById(@Query("Id") Id: Int): hotel


    @POST("Adventure/Add") // This should match the route in your .NET controller
    suspend fun addAdventure(@Body adventure: adventures):Response<Int>


}