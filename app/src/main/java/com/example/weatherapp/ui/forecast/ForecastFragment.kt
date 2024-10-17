package com.example.weatherapp.ui.forecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.ListForecastAdapter
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentForecastBinding
import com.example.weatherapp.source.response.Weather
import timber.log.Timber


class ForecastFragment : Fragment() {

    private lateinit var binding: FragmentForecastBinding
    private lateinit var rvWeather: RecyclerView
    private val list = ArrayList<Weather>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvWeather = binding.recyclerView
        rvWeather.setHasFixedSize(true)

        list.addAll(getListWeather())
        showRecycleView()

    }

    private fun getListWeather(): ArrayList<Weather> {
        val dataTime = resources.getStringArray(R.array.time_labels)
        val dataTemperature = resources.getStringArray(R.array.temperature_values)
        val dataPhoto = resources.obtainTypedArray(R.array.weather_icons)
        val listWeather = ArrayList<Weather>()
        for (i in dataTime.indices) {
            val weather = Weather(dataTime[i], dataTemperature[i], dataPhoto.getResourceId(i, -1))
            listWeather.add(weather)
        }
        return listWeather
    }

    private fun showRecycleView(){
        rvWeather.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val listForecastAdapter = ListForecastAdapter(getListWeather())
        rvWeather.adapter = listForecastAdapter

    }

}