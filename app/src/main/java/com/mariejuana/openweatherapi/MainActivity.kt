package com.mariejuana.openweatherapi

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mariejuana.openweatherapi.adapter.FiveDayForecastAdapter
import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.constant.API.API_KEY
import com.mariejuana.openweatherapi.databinding.ActivityMainBinding
import com.mariejuana.openweatherapi.helpers.Helpers
import com.mariejuana.openweatherapi.helpers.RetrofitHelper
import com.mariejuana.openweatherapi.models.Coord
import com.mariejuana.openweatherapi.models.Forecast
import com.mariejuana.openweatherapi.models.ForecastMain
import com.mariejuana.openweatherapi.models.Weather
import com.mariejuana.openweatherapi.repositories.CurrentWeather
import com.mariejuana.openweatherapi.repositories.WeatherForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.create
import java.util.ArrayList

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FiveDayForecastAdapter
    private lateinit var weatherData: ArrayList<Weather>
    private lateinit var forecastData: ArrayList<Forecast>
    private lateinit var mainForecast: ArrayList<ForecastMain>
    private lateinit var location: ArrayList<Coord>
    private lateinit var weather: Weather
    private lateinit var forecast: Forecast
    private val typeConverter = Helpers()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forecastData = arrayListOf()
        adapter = FiveDayForecastAdapter(forecastData, this)

        location = arrayListOf(
            Coord("Manila",14.5964838,120.8971588),
            Coord("Angeles City", 15.1463554, 120.5245999),
            Coord("Cebu City", 10.317, 123.891)
        )

        val cityListSpinner = arrayOf("Manila", "Angeles City", "Cebu City")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, cityListSpinner)
        binding.locationSpinner.adapter = spinnerAdapter
        binding.locationSpinner.onItemSelectedListener = this

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvForecast.layoutManager = layoutManager
            rvForecast.adapter = adapter
        }

    }

    private fun weatherData(lat: Double, lon: Double) {
        super.onResume()

        val forecastInitiate = RetrofitHelper.getInstance().create(WeatherForecast::class.java)
        val weatherInitiate = RetrofitHelper.getInstance().create(CurrentWeather::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            val resultForecast = forecastInitiate.getFiveDayForecast(lat, lon, API_KEY, "metric")
            val resultWeather = weatherInitiate.getCurrentWeather(lat, lon, API_KEY, "metric")
            val weatherDataExtras = resultWeather.body()
            val weatherData = resultForecast.body()

            if (weatherData != null) {
                forecast = weatherData.list[0]
                forecastData.clear()
                forecastData.addAll(weatherData.list)

                withContext(Dispatchers.Main){
                    adapter.updateWeather(forecastData)
                    Glide.with(this@MainActivity)
                        .load(API.BASE_WEATHER_IMG_URL+ weatherDataExtras?.weather?.get(0)?.icon+"@2x.png")
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .override(200,200)
                        .into(binding.imgWeather)
                }
            }

            with(binding) {
//                txtWeatherType.text = String.format("%s", weatherDataExtras?.main)
                txtDateTime.text = String.format("%s | %s",
                    weatherDataExtras?.dt?.let { typeConverter.getDay(it.toLong()) },
                    weatherDataExtras?.dt?.let { typeConverter.getTime(it.toLong()) })
                txtTemp.text = String.format("%s℃", weatherDataExtras?.main?.temp.toString())
                txtMinTemp.text = String.format("%s℃",  weatherDataExtras?.main?.temp_min.toString())
                txtMaxTemp.text = String.format("%s℃",  weatherDataExtras?.main?.temp_max.toString())
                txtFeelsTemp.text = String.format("Feels like %s℃",  weatherDataExtras?.main?.feels_like.toString())
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val location = location[position]
        location.lat?.let { location.lon?.let { it1 -> weatherData(it, it1) } }

        val locationName = parent?.getItemAtPosition(position).toString()
        binding.txtLocation.text = locationName
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}