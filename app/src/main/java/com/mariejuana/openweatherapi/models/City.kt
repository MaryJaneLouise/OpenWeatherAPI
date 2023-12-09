package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class City (
    var coord: Coord,
    var country: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var population: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var timezone: Int? = null
) : Serializable