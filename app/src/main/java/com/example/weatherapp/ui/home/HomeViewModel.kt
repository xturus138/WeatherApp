package com.example.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.source.response.WeatherResponse
import com.example.weatherapp.source.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    fun getWeatherData(apiKey: String, location: String) {
        Timber.d("API call with apiKey: $apiKey and location: $location")

        val client = ApiConfig.getApiService().getWeather(apiKey, location)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    Timber.d("API response success: ${response.body()}")
                    _weatherData.value = response.body()
                } else {
                    Timber.e("API response failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Timber.e("API call failed: ${t.message}")
            }
        })
    }






}

