package com.example.weatherapp.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.source.response.ForecastdayItem
import com.example.weatherapp.source.response.WeatherResponse
import com.example.weatherapp.source.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ForecastViewModel : ViewModel() {
    private val _forecastList = MutableLiveData<List<ForecastdayItem>>()
    val forecastList: LiveData<List<ForecastdayItem>> = _forecastList
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _locationResponse = MutableLiveData<String>()
    val locationResponse: LiveData<String> = _locationResponse


    fun getForecastData(location: String, days: Int) {
        if (location.isEmpty()) {
            _errorMessage.value = "Please enter a location"
            return
        }
        _isLoading.value = true
        val client = ApiConfig.getApiService().getWeather(BuildConfig.API_KEY, location, days)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        _forecastList.value = weatherResponse.forecast.forecastday
                        _locationResponse.value = weatherResponse.location.name
                        Timber.d("API response forecast list success!")

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
                _isLoading.value = false
                Timber.e("API call failed: ${t.message}")
                _errorMessage.value = "API call failed: ${t.message}"
            }
        })
    }

}
