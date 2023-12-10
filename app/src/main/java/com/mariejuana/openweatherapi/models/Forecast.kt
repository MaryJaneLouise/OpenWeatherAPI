package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class Forecast(
    var clouds: Cloud,
    var dt: Int? = null,
    var dt_string: String? = null,
    var main: Main,
    var sys: Sys,
    var visibility: Int? = null,
    var weather: ArrayList<Weather>,
    var wind: Wind,
    var city : City,
) : Serializable
