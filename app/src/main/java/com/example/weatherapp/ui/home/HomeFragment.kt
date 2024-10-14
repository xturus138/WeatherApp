package com.example.weatherapp.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var buttonClick : ImageButton
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val apiKey = "8f589eb621634745aef75038240910"
    private val LOCATION_PERMISSION_REQUEST_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.d("onViewCreated Running")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation(apiKey)
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission Denied, Please allow location permission", Toast.LENGTH_SHORT).show()
            }
        }


        binding.progressHome.visibility = View.VISIBLE
        buttonClick()
        getLocation(apiKey)

        binding.swipeRefresh.setOnRefreshListener {
            binding.apply {
                progressHome.visibility = View.VISIBLE
                hideUi()
            }
            getLocation(apiKey)
            Timber.d("Swipe Refresh")
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun buttonClick(){
        Timber.d("buttonClick Running")
        buttonClick = binding.locationLogo
        buttonClick.setOnClickListener{
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.dialog_layout, null)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun getLocation(apiKey: String) {
        if (!isLocationEnabled()) {
            Timber.d("Location Is Disabled")
            Toast.makeText(requireContext(), "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
        } else {
            Timber.d("Location Is Enabled Function 1")
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                Timber.d("Location Is Enabled Function 2")
                fusedLocationClient.lastLocation
                    .addOnSuccessListener(requireActivity()) { location ->
                        if (location != null) {
                            Timber.d("Location: ${location.latitude} ${location.longitude}")
                            val locationString = "${location.latitude},${location.longitude}"
                            getWeatherData(apiKey, locationString)
                        } else {
                            newLocation()
                            Toast.makeText(requireContext(), "Please Wait", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    //dari medium
    private fun newLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                    val result = fusedLocationClient.getCurrentLocation(
                        priority,
                        CancellationTokenSource().token,
                    ).await()

                    result?.let { fetchedLocation ->
                        withContext(Dispatchers.Main) {
                            val locationString = "${fetchedLocation.latitude},${fetchedLocation.longitude}"
                            getWeatherData(apiKey, locationString)
                        }
                    }
                } catch (e: SecurityException) {
                    Timber.e("LocationError", "Security exception while fetching location", e)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            Timber.e(ex)
            false
        }
    }






    private fun getWeatherData(apiKey: String, location: String) {
        Timber.d("getWeatherData Running")
        homeViewModel.getWeatherData(apiKey, location)
        homeViewModel.weatherData.observe(viewLifecycleOwner) { weatherResponse ->
            if (weatherResponse != null) {
                Timber.d("weatherResponse True")
                binding.apply {
                    progressHome.visibility = View.GONE
                    visibleUi()
                    address.text = weatherResponse.location.name
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

    private fun visibleUi() {
        binding.apply {
            locationLogo.visibility = View.VISIBLE
            tempMin.visibility = View.VISIBLE
            tempMax.visibility = View.VISIBLE
            detailsContainer.visibility = View.VISIBLE
            temp.visibility = View.VISIBLE
            status.visibility = View.VISIBLE
            addressContainer.visibility = View.VISIBLE
        }
    }

    private fun hideUi() {
        binding.apply {
            locationLogo.visibility = View.GONE
            tempMin.visibility = View.GONE
            tempMax.visibility = View.GONE
            detailsContainer.visibility = View.GONE
            temp.visibility = View.GONE
            status.visibility = View.GONE
            addressContainer.visibility = View.GONE
        }
    }


}

