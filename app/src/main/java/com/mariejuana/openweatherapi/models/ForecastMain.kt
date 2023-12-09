package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class ForecastMain(
    var count: Int? = null,
    var cod: String? = null,
    var message: Int? = null,
    var list : ArrayList<Forecast>,
    var city : City,
) : Serializable
