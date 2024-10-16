package com.example.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.source.response.WeatherResponse
import com.example.weatherapp.source.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    private val _errorMessage = MutableLiveData<String>()
    val weatherData: LiveData<WeatherResponse> = _weatherData
    val errorMessage: LiveData<String> = _errorMessage

    fun getWeatherData(location: String, days: Int) {
        val client = ApiConfig.getApiService().getWeather(BuildConfig.API_KEY, location, days)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    Timber.d("API response success: ${response.body()}")
                    _weatherData.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Timber.e("API response failed: $errorBody")
                    _errorMessage.value = "Failed to load data: $errorBody"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Timber.e("API call failed: ${t.message}")
            }
        })
    }






}

