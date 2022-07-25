package com.nab.weatherforecast.ui

import android.content.ClipData.Item
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nab.weatherforecast.databinding.DailyForecastItemBinding


class DailyForecastViewHolder(private val binding: DailyForecastItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind (itemModel: DailyForecastItemViewModel) {
        binding.itemModel = itemModel
        binding.executePendingBindings()
    }
}