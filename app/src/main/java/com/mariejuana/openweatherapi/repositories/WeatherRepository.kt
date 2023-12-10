package com.mariejuana.openweatherapi.repositories

import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.helpers.RetrofitHelper
import com.mariejuana.openweatherapi.models.Forecast
import com.mariejuana.openweatherapi.models.Weather
import com.mariejuana.openweatherapi.models.selected.Main
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class WeatherRepository {
    fun getCurrentWeather(lat: Double, lon: Double): Flow<Response<Forecast>> = flow {
        val r = RetrofitHelper.getInstance().create(CurrentWeather::class.java).getCurrentWeather(lat ,lon, API.API_KEY,"metric")
        emit(r)
    }.flowOn(Dispatchers.IO)
}