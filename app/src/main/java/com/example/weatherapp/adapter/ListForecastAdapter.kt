package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ItemForecastBinding
import com.example.weatherapp.source.response.ForecastdayItem

class ListForecastAdapter:ListAdapter<ForecastdayItem, ListForecastAdapter.MyViewHolder>(
    DIFF_CALLBACK
){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemForecastBinding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val forecastItem = getItem(position)
        holder.bind(forecastItem)
    }

    class MyViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastItem: ForecastdayItem) {
            binding.textTime.text = forecastItem.date
            binding.textTemperature.text = "${forecastItem.day.condition.text}"

            val iconUrl = "https:${forecastItem.day.condition.icon}"
            Glide.with(binding.root.context)
                .load(iconUrl)
                .into(binding.imageLogo)
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForecastdayItem>(){
            override fun areItemsTheSame(oldItem: ForecastdayItem, newItem: ForecastdayItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ForecastdayItem, newItem: ForecastdayItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}
