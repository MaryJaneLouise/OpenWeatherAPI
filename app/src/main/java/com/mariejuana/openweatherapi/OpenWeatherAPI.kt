package com.mariejuana.openweatherapi

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors

// This is used to enable the dynamic color for Android 12+
// This is just for XML. For Jetpack Compose, it can be set in Themes.kt
class OpenWeatherAPI : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context
            private set

    }
}