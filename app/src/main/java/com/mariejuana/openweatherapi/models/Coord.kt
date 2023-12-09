package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class Coord(
    var location: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
) : Serializable

