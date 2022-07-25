package com.nab.weatherforecast.ui

data class DailyForecastItemViewModel(
    val date: String,
    val temperature: String,
    val pressure: String,
    val humidity: String,
    val weatherDescription: String,
)