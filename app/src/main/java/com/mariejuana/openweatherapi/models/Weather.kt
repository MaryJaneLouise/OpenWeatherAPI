package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class Weather (
    var id: Int? = null,
    var main: String,
    var description: String? = null,
    var icon: String? = null
) : Serializable