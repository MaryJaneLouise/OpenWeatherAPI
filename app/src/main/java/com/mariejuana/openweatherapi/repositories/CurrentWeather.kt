package com.mariejuana.openweatherapi.repositories

import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.models.Forecast
import com.mariejuana.openweatherapi.models.selected.Main
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeather {
    @GET(API.CURRENT_WEATHER)
    suspend fun getCurrentWeather(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Response<Forecast>

}