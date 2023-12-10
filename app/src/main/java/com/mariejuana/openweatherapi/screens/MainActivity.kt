package com.mariejuana.openweatherapi.screens

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mariejuana.openweatherapi.adapter.FiveDayForecastAdapter
import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.constant.API.API_KEY
import com.mariejuana.openweatherapi.constant.StateAPI
import com.mariejuana.openweatherapi.databinding.ActivityMainBinding
import com.mariejuana.openweatherapi.helpers.Helpers
import com.mariejuana.openweatherapi.helpers.RetrofitHelper
import com.mariejuana.openweatherapi.helpers.SharedData
import com.mariejuana.openweatherapi.models.Coord
import com.mariejuana.openweatherapi.models.Forecast
import com.mariejuana.openweatherapi.models.ForecastMain
import com.mariejuana.openweatherapi.models.Weather
import com.mariejuana.openweatherapi.repositories.CurrentWeather
import com.mariejuana.openweatherapi.repositories.WeatherForecast
import com.mariejuana.openweatherapi.repositories.WeatherRepository
import com.mariejuana.openweatherapi.screens.viewmodels.MainActivityViewModel
import com.mariejuana.openweatherapi.screens.viewmodels.factory.MainActivityViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
//    private lateinit var viewModel: MainActivityViewModel

    private val typeConverter = Helpers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this,
//            MainActivityViewModelFactory(WeatherRepository()))[MainActivityViewModel::class.java]

        // Loads the Lottie thing screen
        loadSplashScreen()

        // Sets the array list for the recyclerview for 5 - day weather forecast
        forecastData = arrayListOf()
        adapter = FiveDayForecastAdapter(forecastData, this)

        // Queries the selected location using the name, latitude, and longitude
        // Uses Coord model as a guide
        location = arrayListOf(
            Coord("Angeles City", 15.1463554, 120.5245999),
            Coord("Tarlac City", 15.489, 120.599),
            Coord("Manila",14.5964838,120.8971588),
            Coord("Cebu City", 10.317, 123.891),
            Coord("Surigao City", 9.750, 125.500)
        )

        // Loads the array list and feeds them into the spinner / dropdown list
        val cityListSpinner = arrayOf(
            "Angeles City, Pampanga",
            "Tarlac City, Tarlac",
            "Manila, NCR",
            "Cebu City, Cebu",
            "Surigao City, Surigao Del Norte")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, cityListSpinner)
        binding.locationSpinner.adapter = spinnerAdapter
        binding.locationSpinner.onItemSelectedListener = this

        // Loads the recyclerview to be able to load them after creating the view
        // It loads horizontally, otherwise it will be no good when loaded vertically
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvForecast.layoutManager = layoutManager
            rvForecast.adapter = adapter
        }

        // Disabled since the group has decided not to use viewModels
//        lifecycleScope.launch {
//            viewModel.weatherVMState.collect{
//                when(it){
//                    is StateAPI.Loading ->{
//                        binding.animationView.visibility = View.VISIBLE
//                    }
//                    is StateAPI.Success ->{
//                        binding.animationView.visibility = View.GONE
//                    }
//                    is StateAPI.Failure ->{
//                        it.e.printStackTrace()
//                    }
//                    is StateAPI.Empty ->{
//                        binding.cardWeather.visibility = View.GONE
//                        binding.animationView.visibility = View.VISIBLE
//                    }
//                }
//            }
//        }
    }

    // Loads the data for the current weather and 5 - day weather forecast for the selected location
    private fun weatherData(lat: Double, lon: Double, locationName: String) {
        val forecastInitiate = RetrofitHelper.getInstance().create(WeatherForecast::class.java)
        val weatherInitiate = RetrofitHelper.getInstance().create(CurrentWeather::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            val resultForecast = forecastInitiate.getFiveDayForecast(lat, lon, API_KEY, "metric")
            val resultWeather = weatherInitiate.getCurrentWeather(lat, lon, API_KEY, "metric")
            val weatherDataExtras = resultWeather.body()
            val weatherData = resultForecast.body()

            // Loads the 5 - day forecast for the recyclerview
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

            // Loads the current weather forecast for the selected location
            with(binding) {
                // Loads the data for the first card
                // Important details like weather type, time and date, and "feels like" temperature
                txtWeatherType.text = String.format("%s", weatherDataExtras?.weather?.get(0)?.main.toString())
                txtFeelsTemp.text = String.format("Feels like %s\u2103",  weatherDataExtras?.main?.feels_like.toString())
                txtDateTime.text = String.format("%s | %s",
                    weatherDataExtras?.dt?.let { typeConverter.getDay(it.toLong()) },
                    weatherDataExtras?.dt?.let { typeConverter.getTime(it.toLong()) })

                // Loads the secondary three cards for the temperature
                // Details like current temperature, minimum temperature, and max temperature
                txtTemp.text = String.format("%s\u2103", weatherDataExtras?.main?.temp.toString())
                txtMinTemp.text = String.format("%s\u2103",  weatherDataExtras?.main?.temp_min.toString())
                txtMaxTemp.text = String.format("%s\u2103",  weatherDataExtras?.main?.temp_max.toString())

                // Sets the intent or the passing of the data from this MainActivity to WeatherInfoFull screen
                // This passes two objects at once: selected location name and its weather information details
                btnShowMore.setOnClickListener {
                    var intent = Intent(this@MainActivity, WeatherInfoFull::class.java)
                    intent.putExtra("locationName", locationName)
                    intent.putExtra("itemData", weatherDataExtras)
                    startActivity(intent)
                }

                // Set image for the current temperature
                if (weatherDataExtras?.main?.temp.toString().toDouble() <= 35.00) {
                    imgCurrentTempPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.cold_temp)
                } else {
                    imgCurrentTempPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.hot_temp)
                }

                // Set image for the minimum temperature
                if (weatherDataExtras?.main?.temp_min.toString().toDouble() <= 35.00) {
                    imgTempMinPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.cold_temp)
                } else {
                    imgTempMinPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.hot_temp)
                }

                // Set image for the maximum temperature
                if (weatherDataExtras?.main?.temp_max.toString().toDouble() <= 35.00) {
                    imgTempMaxPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.cold_temp)
                } else {
                    imgTempMaxPlace.setImageResource(com.mariejuana.openweatherapi.R.drawable.hot_temp)
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Gets the position and the name of the selected position
        val location = location[position]
        val locationName = parent?.getItemAtPosition(position).toString()

        // Passes the said information to the weatherData function in order to feed it two combined APIs
        location.lat?.let { location.lon?.let { it1 -> weatherData(it, it1, locationName) } }

        // Saves temporarily the location name in the memory
        // Used to access to different parts of the app
        SharedData.locationName = locationName
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do.. literally. Just nothing
    }

    // Loads the Lottie thing
    private fun loadSplashScreen() {
        binding.animationView.isVisible = true
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Nothing to do.. literally
            }

            override fun onFinish() {
                binding.animationView.isVisible = false
            }
        }.start()
    }
}