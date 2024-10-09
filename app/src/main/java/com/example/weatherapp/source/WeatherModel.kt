package com.example.weatherapp.source

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localTime: String
)

data class Current(
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val is_day: Int,
    val condition: Condition,
    val wind_kph: Double,
    val pressure_mb: Double,
    val precip_mm: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Double,
    val windchill_c: Double,
    val heatindex_c: Double,
    val dewpoint_c: Double,
    val vis_km: Double,
    val gust_kph: Double
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)



