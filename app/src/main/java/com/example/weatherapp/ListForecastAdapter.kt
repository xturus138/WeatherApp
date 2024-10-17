package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.source.response.Weather

class ListForecastAdapter(private val listForecast: ArrayList<Weather>): RecyclerView.Adapter<ListForecastAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listForecast.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (time, temperature, photo) = listForecast[position]
        holder.imagePhoto.setImageResource(photo)
        holder.textTime.text = time
        holder.textTemperature.text = temperature
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePhoto: ImageView = itemView.findViewById(R.id.imageLogo)
        val textTime: TextView = itemView.findViewById(R.id.textTime)
        val textTemperature: TextView = itemView.findViewById(R.id.textTemperature)
    }

}
