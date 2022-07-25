package com.nab.weatherforecast.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.nab.weatherforecast.databinding.DailyForecastItemBinding

class DailyForecastAdapter :
    ListAdapter<DailyForecastItemViewModel, DailyForecastViewHolder>(ItemViewModelDiffCallback()) {
    private lateinit var binding: DailyForecastItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        binding =
            DailyForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ItemViewModelDiffCallback : DiffUtil.ItemCallback<DailyForecastItemViewModel>() {
    override fun areItemsTheSame(
        oldItem: DailyForecastItemViewModel,
        newItem: DailyForecastItemViewModel
    ): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(
        oldItem: DailyForecastItemViewModel,
        newItem: DailyForecastItemViewModel
    ): Boolean =
        oldItem == newItem
}