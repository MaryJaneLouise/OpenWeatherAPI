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
    // private lateinit var viewModel: MainActivityViewModel

    private val typeConverter = Helpers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this,
//            MainActivityViewModelFactory(WeatherRepository()))[MainActivityViewModel::class.java]

        loadSplashScreen()

        forecastData = arrayListOf()
        adapter = FiveDayForecastAdapter(forecastData, this)

        location = arrayListOf(
            Coord("Angeles City", 15.1463554, 120.5245999),
            Coord("Tarlac City", 15.489, 120.599),
            Coord("Manila",14.5964838,120.8971588),
            Coord("Cebu City", 10.317, 123.891),
            Coord("Surigao City", 9.750, 125.500)
        )

        val cityListSpinner = arrayOf(
            "Angeles City, Pampanga",
            "Tarlac City, Tarlac",
            "Manila, NCR",
            "Cebu City, Cebu",
            "Surigao City, Surigao Del Norte")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, cityListSpinner)
        binding.locationSpinner.adapter = spinnerAdapter
        binding.locationSpinner.onItemSelectedListener = this

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvForecast.layoutManager = layoutManager
            rvForecast.adapter = adapter
        }


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

    private fun weatherData(lat: Double, lon: Double, locationName: String) {
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
                txtWeatherType.text = String.format("%s", weatherDataExtras?.weather?.get(0)?.main.toString())
                txtDateTime.text = String.format("%s | %s",
                    weatherDataExtras?.dt?.let { typeConverter.getDay(it.toLong()) },
                    weatherDataExtras?.dt?.let { typeConverter.getTime(it.toLong()) })
                txtTemp.text = String.format("%s\u2103", weatherDataExtras?.main?.temp.toString())
                txtMinTemp.text = String.format("%s\u2103",  weatherDataExtras?.main?.temp_min.toString())
                txtMaxTemp.text = String.format("%s\u2103",  weatherDataExtras?.main?.temp_max.toString())
                txtFeelsTemp.text = String.format("Feels like %s\u2103",  weatherDataExtras?.main?.feels_like.toString())

                btnShowMore.setOnClickListener {
                    var intent = Intent(this@MainActivity, WeatherInfoFull::class.java)
                    intent.putExtra("locationName", locationName)
                    intent.putExtra("itemData", weatherDataExtras)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val location = location[position]
        val locationName = parent?.getItemAtPosition(position).toString()
        location.lat?.let { location.lon?.let { it1 -> weatherData(it, it1, locationName) } }

        SharedData.locationName = locationName
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Nothing to do.. literally
    }

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