package com.example.weatherapp.ui.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ListForecastAdapter
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentForecastBinding
import com.example.weatherapp.source.retrofit.ApiConfig
import timber.log.Timber


class ForecastFragment : Fragment() {

    private lateinit var binding: FragmentForecastBinding
    private val forecastViewModel: ForecastViewModel by viewModels()
    private lateinit var adapter: ListForecastAdapter

    companion object{
        private const val TAG = "ForecastFragment"
        private const val LOCATION = "Bandung"
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

        showForecast(LOCATION, DAYS)

        forecastViewModel.forecastList.observe(viewLifecycleOwner) { forecast ->
            adapter.submitList(forecast)
        }

        forecastViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Timber.e(errorMessage)
        }

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

    private fun showForecast(location: String, days: Int) {
        showLoading()
        forecastViewModel.getForecastData(location, days)
        stopLoading()
    }

    private fun showLoading(){
        binding.progressForecast.visibility = View.VISIBLE
    }

    private fun stopLoading(){
        binding.progressForecast.visibility = View.GONE
    }




}