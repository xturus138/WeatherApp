package com.example.weatherapp.ui.home

import android.content.Context
import android.location.LocationManager
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
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getWeatherData(location: String, days: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getWeather(BuildConfig.API_KEY, location, days)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Timber.d("API response current weather succsess!")
                    _weatherData.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Timber.e("API response failed: $errorBody")
                    _errorMessage.value = "Failed to load data: $errorBody"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _isLoading.value = false
                Timber.e("API call failed: ${t.message}")
            }
        })
    }

    fun isLocationEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
            false
        }
    }






}

