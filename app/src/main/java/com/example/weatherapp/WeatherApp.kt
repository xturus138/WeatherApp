package com.example.weatherapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class WeatherApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}