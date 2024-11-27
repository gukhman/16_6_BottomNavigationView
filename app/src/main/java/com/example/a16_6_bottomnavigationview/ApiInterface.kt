package com.example.a16_6_bottomnavigationview
import com.example.a16_6_bottomnavigationview.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q")city: String,
        @Query("units")units: String,
        @Query("appid")apiKey: String
    ) : Response<CurrentWeather>

    @GET("weather?")
    suspend fun getCurrentWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String
    ): Response<CurrentWeather>
}