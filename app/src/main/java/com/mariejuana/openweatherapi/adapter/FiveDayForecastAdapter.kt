package com.mariejuana.openweatherapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mariejuana.openweatherapi.constant.API
import com.mariejuana.openweatherapi.models.Forecast
import com.mariejuana.openweatherapi.databinding.ContentForecastRvBinding
import com.mariejuana.openweatherapi.helpers.Helpers
import java.io.Serializable


class FiveDayForecastAdapter (private var forecastList: ArrayList<Forecast>, private var context: Context) : RecyclerView.Adapter<FiveDayForecastAdapter.ForecastViewHolder>(), Serializable {
    private val typeConverter = Helpers()

    inner class ForecastViewHolder(private val binding: ContentForecastRvBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: Forecast) {
            val currentWeather = itemData.weather[0]

            with(binding) {
                txtDay.text = itemData.dt?.let { typeConverter.getDay(it.toLong()) }
                txtTime.text = itemData.dt?.let { typeConverter.getTime(it.toLong()) }
                txtTemp.text = String.format("%s\u2103",itemData.main.temp)
                txtForecast.text = currentWeather.main
            }

            Glide.with(context)
                .load(API.BASE_WEATHER_IMG_URL+ currentWeather.icon+"@2x.png")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(200,200)
                .into(binding.imgWeather)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ContentForecastRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecastData = forecastList[position]
        holder.bind(forecastData)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    fun updateWeather(forecastList: ArrayList<Forecast>){
        this.forecastList = arrayListOf()
        notifyDataSetChanged()
        this.forecastList = forecastList
        this.notifyItemInserted(this.forecastList.size)
    }
}