package com.example.weatherapp.source.response

import android.content.Context
import android.location.LocationManager

interface LocationProvider {
    fun getLocationManager(): LocationManager
    fun getContextForLocation(): Context
}