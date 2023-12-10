package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class Main(
    var feels_like: Double? = null,
    var humidity: Int? = null,
    var pressure: Int? = null,
    var temp: Double? = null,
    var temp_min: Double? = null,
    var temp_max: Double? = null,
    var temp_mean: Double? = null,
) : Serializable