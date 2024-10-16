package com.example.weatherapp.source.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("current")
	val current: Current,

	@field:SerializedName("location")
	val location: Location,

	@field:SerializedName("forecast")
	val forecast: Forecast
)

data class HourItem(

	@field:SerializedName("feelslike_c")
	val feelslikeC: Double,

	@field:SerializedName("feelslike_f")
	val feelslikeF: Double,

	@field:SerializedName("wind_degree")
	val windDegree: Double,

	@field:SerializedName("windchill_f")
	val windchillF: Double,

	@field:SerializedName("windchill_c")
	val windchillC: Double,

	@field:SerializedName("temp_c")
	val tempC: Double,

	@field:SerializedName("temp_f")
	val tempF: Double,

	@field:SerializedName("cloud")
	val cloud: Int,

	@field:SerializedName("wind_kph")
	val windKph: Double,

	@field:SerializedName("wind_mph")
	val windMph: Double,

	@field:SerializedName("snow_cm")
	val snowCm: Int,

	@field:SerializedName("humidity")
	val humidity: Int,

	@field:SerializedName("dewpoint_f")
	val dewpointF: Double,

	@field:SerializedName("will_it_rain")
	val willItRain: Int,

	@field:SerializedName("uv")
	val uv: Double,

	@field:SerializedName("heatindex_f")
	val heatindexF: Double,

	@field:SerializedName("dewpoint_c")
	val dewpointC: Double,

	@field:SerializedName("is_day")
	val isDay: Int,

	@field:SerializedName("precip_in")
	val precipIn: Double,

	@field:SerializedName("heatindex_c")
	val heatindexC: Double,

	@field:SerializedName("wind_dir")
	val windDir: String,

	@field:SerializedName("gust_mph")
	val gustMph: Double,

	@field:SerializedName("pressure_in")
	val pressureIn: Double,

	@field:SerializedName("chance_of_rain")
	val chanceOfRain: Int,

	@field:SerializedName("gust_kph")
	val gustKph: Double,

	@field:SerializedName("precip_mm")
	val precipMm: Double,

	@field:SerializedName("condition")
	val condition: Condition,

	@field:SerializedName("will_it_snow")
	val willItSnow: Int,

	@field:SerializedName("vis_km")
	val visKm: Int,

	@field:SerializedName("time_epoch")
	val timeEpoch: Int,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("chance_of_snow")
	val chanceOfSnow: Int,

	@field:SerializedName("pressure_mb")
	val pressureMb: Int,

	@field:SerializedName("vis_miles")
	val visMiles: Int
)

data class Current(

	@field:SerializedName("feelslike_c")
	val feelslikeC: Double,

	@field:SerializedName("feelslike_f")
	val feelslikeF: Double,

	@field:SerializedName("wind_degree")
	val windDegree: Double,

	@field:SerializedName("windchill_f")
	val windchillF: Double,

	@field:SerializedName("windchill_c")
	val windchillC: Double,

	@field:SerializedName("last_updated_epoch")
	val lastUpdatedEpoch: Double,

	@field:SerializedName("temp_c")
	val tempC: Double,

	@field:SerializedName("temp_f")
	val tempF: Double,

	@field:SerializedName("cloud")
	val cloud: Double,

	@field:SerializedName("wind_kph")
	val windKph: Double,

	@field:SerializedName("wind_mph")
	val windMph: Double,

	@field:SerializedName("humidity")
	val humidity: Double,

	@field:SerializedName("dewpoint_f")
	val dewpointF: Double,

	@field:SerializedName("uv")
	val uv: Double,

	@field:SerializedName("last_updated")
	val lastUpdated: String,

	@field:SerializedName("heatindex_f")
	val heatindexF: Double,

	@field:SerializedName("dewpoint_c")
	val dewpointC: Double,

	@field:SerializedName("is_day")
	val isDay: Int,

	@field:SerializedName("precip_in")
	val precipIn: Double,

	@field:SerializedName("heatindex_c")
	val heatindexC: Double,

	@field:SerializedName("wind_dir")
	val windDir: String,

	@field:SerializedName("gust_mph")
	val gustMph: Double,

	@field:SerializedName("pressure_in")
	val pressureIn: Double,

	@field:SerializedName("gust_kph")
	val gustKph: Double,

	@field:SerializedName("precip_mm")
	val precipMm: Double,

	@field:SerializedName("condition")
	val condition: Condition,

	@field:SerializedName("vis_km")
	val visKm: Double,

	@field:SerializedName("pressure_mb")
	val pressureMb: Double,

	@field:SerializedName("vis_miles")
	val visMiles: Double
)

data class Condition(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("text")
	val text: String
)

data class Astro(

	@field:SerializedName("moonset")
	val moonset: String,

	@field:SerializedName("moon_illumination")
	val moonIllumination: Int,

	@field:SerializedName("sunrise")
	val sunrise: String,

	@field:SerializedName("moon_phase")
	val moonPhase: String,

	@field:SerializedName("sunset")
	val sunset: String,

	@field:SerializedName("is_moon_up")
	val isMoonUp: Int,

	@field:SerializedName("is_sun_up")
	val isSunUp: Int,

	@field:SerializedName("moonrise")
	val moonrise: String
)

data class ForecastdayItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("astro")
	val astro: Astro,

	@field:SerializedName("date_epoch")
	val dateEpoch: Int,

	@field:SerializedName("hour")
	val hour: List<HourItem>,

	@field:SerializedName("day")
	val day: Day
)

data class Forecast(

	@field:SerializedName("forecastday")
	val forecastday: List<ForecastdayItem>
)

data class Day(

	@field:SerializedName("avgvis_km")
	val avgvisKm: Double,

	@field:SerializedName("uv")
	val uv: Double,

	@field:SerializedName("avgtemp_f")
	val avgtempF: Double,

	@field:SerializedName("avgtemp_c")
	val avgtempC: Double,

	@field:SerializedName("daily_chance_of_snow")
	val dailyChanceOfSnow: Int,

	@field:SerializedName("maxtemp_c")
	val maxtempC: Double,

	@field:SerializedName("maxtemp_f")
	val maxtempF: Double,

	@field:SerializedName("mintemp_c")
	val mintempC: Double,

	@field:SerializedName("avgvis_miles")
	val avgvisMiles: Double,

	@field:SerializedName("daily_will_it_rain")
	val dailyWillItRain: Int,

	@field:SerializedName("mintemp_f")
	val mintempF: Double,

	@field:SerializedName("totalprecip_in")
	val totalprecipIn: Double,

	@field:SerializedName("totalsnow_cm")
	val totalsnowCm: Int,

	@field:SerializedName("avghumidity")
	val avghumidity: Int,

	@field:SerializedName("condition")
	val condition: Condition,

	@field:SerializedName("maxwind_kph")
	val maxwindKph: Any,

	@field:SerializedName("maxwind_mph")
	val maxwindMph: Any,

	@field:SerializedName("daily_chance_of_rain")
	val dailyChanceOfRain: Int,

	@field:SerializedName("totalprecip_mm")
	val totalprecipMm: Any,

	@field:SerializedName("daily_will_it_snow")
	val dailyWillItSnow: Int
)

data class Location(

	@field:SerializedName("localtime")
	val localtime: String,

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("localtime_epoch")
	val localtimeEpoch: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("lon")
	val lon: Any,

	@field:SerializedName("region")
	val region: String,

	@field:SerializedName("lat")
	val lat: Any,

	@field:SerializedName("tz_id")
	val tzId: String
)
