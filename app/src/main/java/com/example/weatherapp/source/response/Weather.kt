package com.example.weatherapp.source.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    val time: String,
    val temperature: String,
    val photo: Int
) : Parcelable
