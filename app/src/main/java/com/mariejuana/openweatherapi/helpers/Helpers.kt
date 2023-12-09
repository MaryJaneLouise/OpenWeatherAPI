package com.mariejuana.openweatherapi.helpers

import java.text.SimpleDateFormat
import java.util.Locale

class Helpers {
    fun getDay(timeStamp: Long): String{
        return SimpleDateFormat("EEEE", Locale.ENGLISH).format(timeStamp * 1000)
    }

    fun getTime(timeStamp: Long): String{
        return SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(timeStamp * 1000)
    }
}