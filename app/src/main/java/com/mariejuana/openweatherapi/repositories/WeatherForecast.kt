package com.mariejuana.openweatherapi.repositories

import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.models.ForecastMain
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecast {
    @GET(API.CURRENT_FORECAST)
    suspend fun getFiveDayForecast(
        @Query("lat") lat : Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String
    ) : Response<ForecastMain>
}