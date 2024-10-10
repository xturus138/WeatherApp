package com.example.weatherapp.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
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



        binding.progressHome.visibility = View.VISIBLE
        getLocation(apiKey)


        binding.swipeRefresh.setOnRefreshListener {
            binding.apply {
                progressHome.visibility = View.VISIBLE
                tempMin.visibility = View.GONE
                tempMax.visibility = View.GONE
                detailsContainer.visibility = View.GONE
                temp.visibility = View.GONE
                status.visibility = View.GONE
                addressContainer.visibility = View.GONE
            }
            getLocation(apiKey)
            Timber.d("Swipe Refresh")
            binding.swipeRefresh.isRefreshing = false
        }

    }

    private fun getLocation(apiKey: String) {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!isLocationEnabled()) {
            Toast.makeText(requireContext(), "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.requireContext(), "Wait For 15s", Toast.LENGTH_LONG).show()
            if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f){location ->
                    Timber.d("Location: ${location.latitude} ${location.longitude}")
                    val locationString = "${location.latitude},${location.longitude}"
                    getWeatherData(apiKey, locationString)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
        }

        return gpsEnabled || networkEnabled
    }


    private fun getWeatherData(apiKey: String, location: String) {
        Timber.d("getWeatherData Running")
        homeViewModel.getWeatherData(apiKey, location)
        homeViewModel.weatherData.observe(viewLifecycleOwner) { weatherResponse ->
            if (weatherResponse != null) {
                Timber.d("weatherResponse True")
                binding.apply {
                    progressHome.visibility = View.GONE
                    tempMin.visibility = View.VISIBLE
                    tempMax.visibility = View.VISIBLE
                    detailsContainer.visibility = View.VISIBLE
                    temp.visibility = View.VISIBLE
                    status.visibility = View.VISIBLE
                    addressContainer.visibility = View.VISIBLE
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
}
