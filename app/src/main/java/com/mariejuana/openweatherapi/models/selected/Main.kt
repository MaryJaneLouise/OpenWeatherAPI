package com.mariejuana.openweatherapi.models.selected

import java.io.Serializable

data class Main(
    var feels_like: Double? = null,
    var ground_level: Int? = null,
    var humidity: Int? = null,
    var pressure: Int? = null,
    var sea_level: Int? = null,
    var temp: Double? = null,
    var temp_other: Double? = null,
    var temp_min: Double? = null,
    var temp_max: Double? = null,
) : Serializable