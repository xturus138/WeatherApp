package com.example.weatherapp.ui.home

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val apiKey = "8f589eb621634745aef75038240910"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.d("onViewCreated Running")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.progressHome.visibility = View.VISIBLE
        isLocationPermissionGranted()


        binding.swipeRefresh.setOnRefreshListener {
            isLocationPermissionGranted()
            Timber.d("Swipe Refresh")
            binding.swipeRefresh.isRefreshing = false
        }

    }

    fun getWeatherData(apiKey: String, location: String) {
        Timber.d("getWeatherData Running")
        homeViewModel.getWeatherData(apiKey, location)
        homeViewModel.weatherData.observe(viewLifecycleOwner) { weatherResponse ->
            if (weatherResponse != null) {
                Timber.d("weatherResponse True")
                binding.apply {
                    progressHome.visibility = View.GONE
                    address.text = weatherResponse.location.name
                    //Timber.d("Check Address: ${binding.address.text} and Name: ${weatherResponse.location.name}")
                    updatedAt.text = weatherResponse.current.lastUpdated
                    status.text = weatherResponse.current.condition.text
                    temp.text = weatherResponse.current.tempC.toString()
                    heatIndex.text = weatherResponse.current.heatindexC.toString()
                    windChill.text = weatherResponse.current.windchillC.toString()
                    wind.text = weatherResponse.current.windKph.toString()
                    pressure.text = weatherResponse.current.pressureMb.toString()
                    humidity.text = weatherResponse.current.humidity.toString()
                    visibility.text = weatherResponse.current.cloud.toString()
                }
            }

        }
    }

    private fun isLocationPermissionGranted() {
        Timber.d("isLocationPermissionGranted Running")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
        mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                var latitude = it.latitude
                var longitude = it.longitude
                var location = "$latitude, $longitude"
                Timber.d("Location Found $location")

                getWeatherData(apiKey, location)
            } ?: run {

            }
        }
    }
}
