package com.example.weatherapp.ui.forecast

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ListForecastAdapter
import com.example.weatherapp.R
import com.example.weatherapp.SharedViewModel
import com.example.weatherapp.databinding.FragmentForecastBinding
import com.example.weatherapp.source.retrofit.ApiConfig
import timber.log.Timber


class ForecastFragment : Fragment() {

    private lateinit var binding: FragmentForecastBinding
    private val forecastViewModel: ForecastViewModel by viewModels()
    private lateinit var adapter: ListForecastAdapter
    private val model: SharedViewModel by activityViewModels()
    private lateinit var location: String

    companion object{
        //private const val TAG = "ForecastFragment"
        //private const val LOCATION = "Bandung"
        private const val DAYS = 7
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListForecastAdapter()

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvWeather.layoutManager = layoutManager

        /*val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvWeather.addItemDecoration(itemDecoration)*/

        binding.rvWeather.adapter = adapter

        forecastViewModel.forecastList.observe(viewLifecycleOwner) { forecast ->
            adapter.submitList(forecast)
        }

        forecastViewModel.locationResponse.observe(viewLifecycleOwner) { location ->
            binding.rightText.text = location
        }

        forecastViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Timber.e(errorMessage)
        }

        model.location.observe(viewLifecycleOwner) {
            Timber.d("Location: $it")
            location = it
            showForecast(location, DAYS)

        }

        refresh()

        forecastViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                Timber.d("Loading...")
                showLoading()
            } else {
                Timber.d("Done loading")
                stopLoading()
            }
        }



    }



    private fun refresh(){
        binding.swipeRefresh.setOnRefreshListener {
            Timber.d("Swipe Refresh")
            showForecast(location, DAYS)
        }
    }


    private fun showForecast(location: String, days: Int) {
        forecastViewModel.getForecastData(location, days)
    }

    private fun showLoading(){
        binding.progressForecast.visibility = View.VISIBLE
        binding.rvWeather.visibility = View.GONE
    }

    private fun stopLoading(){
        binding.progressForecast.visibility = View.GONE
        binding.rvWeather.visibility = View.VISIBLE
        binding.swipeRefresh.isRefreshing = false
    }

    //diambil dari geeksforgeeks
    private fun showCustomToast(message: String, durationInMillis: Long) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        }, durationInMillis)
    }




}