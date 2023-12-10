package com.mariejuana.openweatherapi.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mariejuana.openweatherapi.constant.StateAPI
import com.mariejuana.openweatherapi.repositories.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivityViewModel(private var weatherRepository: WeatherRepository): ViewModel() {
    val weatherVMState: MutableStateFlow<StateAPI> = MutableStateFlow(StateAPI.Empty)

    fun getCurrentWeather(lat: Double, lon: Double) = viewModelScope.launch(Dispatchers.IO) {
        weatherVMState.value = StateAPI.Loading
        weatherRepository.getCurrentWeather(lat,lon)
            .catch { e ->
                weatherVMState.value = StateAPI.Failure(e)
            }.collect{ data ->
                weatherVMState.value = StateAPI.Success(data)
            }
    }
}