package com.mariejuana.openweatherapi.models.selected

data class SelectedWeather(
    val id: Int? = null,
    val base: String? = null,
    val clouds: Cloud? = null,
    val cod: Int? = null,
    val coord: Coord? = null,
    val dt: Int? = null,
    val main: Main? = null,
    val name: String? = null,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val visibility: Int? = null,
    val weather: ArrayList<Weather>,
    val wind: Wind
)
