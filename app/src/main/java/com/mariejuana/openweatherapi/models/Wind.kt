package com.mariejuana.openweatherapi.models

import java.io.Serializable

data class Wind (
    var deg: Int? = null,
    var speed: Double? = null,
) : Serializable