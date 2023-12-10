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

        val intent = intent
        val itemData: Forecast? = intent.getSerializableExtra("itemData") as Forecast?

        val extras = intent.extras
        val location = extras?.getString("locationName")

        Glide.with(this@WeatherInfoFull)
            .load(API.BASE_WEATHER_IMG_URL+ itemData?.weather?.get(0)?.icon+"@2x.png")
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .override(200,200)
            .into(binding.imgWeather)

        with(binding) {
            txtLocationName.text = String.format("%s", location.toString())

            txtWeatherType.text = String.format("%s", itemData?.weather?.get(0)?.main.toString())
            txtDateTime.text = String.format("%s | %s",
                itemData?.dt?.let { typeConverter.getDay(it.toLong()) },
                itemData?.dt?.let { typeConverter.getTime(it.toLong()) })
            txtFeelsTemp.text = String.format("Feels like %s\u2103", itemData?.main?.feels_like.toString())

            txtTemp.text = String.format("%s\u2103", itemData?.main?.temp.toString())
            txtMinTemp.text = String.format("%s\u2103", itemData?.main?.temp_min.toString())
            txtMaxTemp.text = String.format("%s\u2103", itemData?.main?.temp_max.toString())

            txtHumidity.text = String.format("%s percent", itemData?.main?.humidity.toString())
            txtPressure.text = String.format("%s hPa", itemData?.main?.pressure.toString())
            txtWindSpeed.text = String.format("%s km/h", itemData?.wind?.speed.toString())
            txtWindDir.text = String.format("%s\u00B0", itemData?.wind?.deg.toString())
        }

    }
}