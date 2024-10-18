package com.example.weatherapp.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.SharedViewModel
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var buttonClick : ImageButton
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val model: SharedViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()



    companion object{
        private const val TAG = "HomeFragment"
        //private const val LOCATION = "Bandung"
        private const val DAYS = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 101
    }

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
        Timber.tag(TAG).d("onViewCreated Running")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission Denied, Please allow location permission", Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                hideUi()
                Timber.tag(TAG).d("Loading...")
                loading()
                showCustomToast("Please Wait", 1000)
            } else {
                visibleUi()
                Timber.tag(TAG).d("Done loading")
                loadingFinish()

            }
        }

        buttonClick()
        getLocation()

        binding.swipeRefresh.setOnRefreshListener {
            getLocation()
            Timber.tag(TAG).d("Swipe Refresh")
            binding.swipeRefresh.isRefreshing = false
        }
    }


    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.modal_search)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val editText = dialog.findViewById<EditText>(R.id.editText)
        val button = dialog.findViewById<Button>(R.id.inputData)
        button.setOnClickListener {
            Timber.tag(TAG).d("Button Clicked Custom Dialog")
            val valueEdit = editText.text.toString()
            if (valueEdit.isEmpty()) {
                Toast.makeText(requireContext(), "Please Enter Your Location", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                model.sendLocation(valueEdit)
                getWeatherData(valueEdit, DAYS)
                Timber.d("editText Value: $valueEdit")
            }
            dialog.dismiss()
            Timber.tag(TAG).d("Dialog Dismiis")
        }
        dialog.show()
    }

    private fun buttonClick(){
        Timber.tag(TAG).d("buttonClick Running")
        buttonClick = binding.locationLogo
        buttonClick.setOnClickListener{
            Timber.tag(TAG).d("buttonClick Clicked")
            showCustomDialog()

        }
    }

    private fun getLocation() {
        if (!homeViewModel.isLocationEnabled(requireContext())) {
            Timber.tag(TAG).d("Location Is Disabled")
            Toast.makeText(requireContext(), "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
        } else {
            Timber.tag(TAG).d("Location Is Enabled Function 1")
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                Timber.tag(TAG).d("Location Is Enabled Function 2")
                fusedLocationClient.lastLocation
                    .addOnSuccessListener(requireActivity()) { location ->
                        if (location != null) {
                            Timber.tag(TAG).d("Location: ${location.latitude} ${location.longitude}")
                            val locationString = "${location.latitude},${location.longitude}"
                            model.sendLocation(locationString)
                            getWeatherData(locationString, DAYS)
                        } else {
                            newLocation()
                            Toast.makeText(requireContext(), "Please Wait", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

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
                            model.sendLocation(locationString)
                            getWeatherData(locationString, DAYS)
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

    //diambil dari geeksforgeeks
    private fun showCustomToast(message: String, durationInMillis: Long) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        }, durationInMillis)
    }


    private fun getWeatherData(location: String, days: Int) {
        Timber.tag(TAG).d("getWeatherData Running")
        homeViewModel.getWeatherData(location, days)
        homeViewModel.weatherData.observe(viewLifecycleOwner) { weatherResponse ->
            if (weatherResponse != null) {
                Timber.tag(TAG).d("weatherResponse Is True!")
                val minTemp = weatherResponse.forecast.forecastday[0].day.mintempC.toInt()
                val maxTemp = weatherResponse.forecast.forecastday[0].day.maxtempC.toInt()
                binding.apply {
                    address.text = weatherResponse.location.name
                    updatedAt.text = weatherResponse.current.lastUpdated
                    status.text = weatherResponse.current.condition.text
                    temp.text = weatherResponse.current.tempC.toString()
                    tempMin.text = getString(R.string.min_temp_text, minTemp)
                    tempMax.text = getString(R.string.max_temp_text, maxTemp)
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

    private fun loading(){
        binding.progressHome.visibility = View.VISIBLE
    }

    private fun loadingFinish(){
        binding.progressHome.visibility = View.GONE
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

