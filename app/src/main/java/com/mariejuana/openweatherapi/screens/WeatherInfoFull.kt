package com.mariejuana.openweatherapi.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.databinding.ActivityWeatherInfoFullBinding
import com.mariejuana.openweatherapi.helpers.Helpers
import com.mariejuana.openweatherapi.models.Forecast

class WeatherInfoFull : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherInfoFullBinding
    private var typeConverter = Helpers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherInfoFullBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets the value of the itemData that are being passed from the MainActivity
        val intent = intent
        val itemData: Forecast? = intent.getSerializableExtra("itemData") as Forecast?

        // Gets the value of the locationName (e. g. Angeles City) that are being passed from the MainActivity
        val extras = intent.extras
        val location = extras?.getString("locationName")

        // Sets the picture of the weather using Glide
        Glide.with(this@WeatherInfoFull)
            .load(API.BASE_WEATHER_IMG_URL+ itemData?.weather?.get(0)?.icon+"@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(200,200)
            .into(binding.imgWeather)

        // Sets the value or image for the respective weather information from a specific location
        with(binding) {
            // Shows the location selected from the MainActivity
            txtLocationName.text = String.format("Forecast for %s", location.toString())

            // Shows the main data for the weather information
            txtWeatherType.text = String.format("%s", itemData?.weather?.get(0)?.main.toString())
            txtDateTime.text = String.format("%s | %s",
                itemData?.dt?.let { typeConverter.getDay(it.toLong()) },
                itemData?.dt?.let { typeConverter.getTime(it.toLong()) })
            txtFeelsTemp.text = String.format("Feels like %s\u2103", itemData?.main?.feels_like.toString())

            // Shows the temperature of the current location
            txtTemp.text = String.format("%s\u2103", itemData?.main?.temp.toString())
            txtMinTemp.text = String.format("%s\u2103", itemData?.main?.temp_min.toString())
            txtMaxTemp.text = String.format("%s\u2103", itemData?.main?.temp_max.toString())

            // Shows the other weather information of the current location
            txtHumidity.text = String.format("%s percent", itemData?.main?.humidity.toString())
            txtPressure.text = String.format("%s hPa", itemData?.main?.pressure.toString())
            txtWindSpeed.text = String.format("%s km/h", itemData?.wind?.speed.toString())
            txtWindDir.text = String.format("%s\u00B0", itemData?.wind?.deg.toString())

            // Set image for the current temperature
            if (itemData?.main?.temp.toString().toDouble() <= 35.00) {
                imgCurrentTempPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.cold_temp)
            } else {
                imgCurrentTempPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.hot_temp)
            }

            // Set image for the minimum temperature
            if (itemData?.main?.temp_min.toString().toDouble() <= 35.00) {
                imgTempMinPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.cold_temp)
            } else {
                imgTempMinPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.hot_temp)
            }

            // Set image for the current temperature
            if (itemData?.main?.temp_max.toString().toDouble() <= 35.00) {
                imgTempMaxPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.cold_temp)
            } else {
                imgTempMaxPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.hot_temp)
            }
        }
    }
}