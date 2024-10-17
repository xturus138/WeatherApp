package com.example.weatherapp.ui.forecast

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

class ForecastViewModel : ViewModel() {
    private val _forecastData = MutableLiveData<WeatherResponse?>()
    private val _errorMessage = MutableLiveData<String>()
    val forecastData: LiveData<WeatherResponse?> = _forecastData
    val errorMessage: LiveData<String> = _errorMessage

    fun getForecastData(location: String, days: Int) {
        if (location.isEmpty()) {
            _errorMessage.value = "Please enter a location"
            return
        }
        val client = ApiConfig.getApiService().getWeather(BuildConfig.API_KEY, location, days)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        Timber.d("API response success: $weatherResponse")
                        _forecastData.value = weatherResponse
                    } else {
                        Timber.e("API response was successful but body is null")
                        _errorMessage.value = "Received empty data"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Timber.e("API response failed: $errorBody")
                    _errorMessage.value = "Failed to load data: $errorBody"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Timber.e("API call failed: ${t.message}")
                _errorMessage.value = "API call failed: ${t.message}"
            }
        })
    }
}
