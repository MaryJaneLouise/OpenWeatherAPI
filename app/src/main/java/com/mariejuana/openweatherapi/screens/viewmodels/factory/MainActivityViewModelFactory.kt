package com.mariejuana.openweatherapi.screens.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mariejuana.openweatherapi.repositories.WeatherRepository
import com.mariejuana.openweatherapi.screens.viewmodels.MainActivityViewModel

class MainActivityViewModelFactory(private val weatherRepository: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(weatherRepository) as T
    }
}