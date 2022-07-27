package com.nab.weatherforecast.domain

sealed class DailyForecastResponse {
    data class Success(val list: List<DailyForecast>) : DailyForecastResponse()
    data class Error(val message: String, val throwable: Throwable? = null) : DailyForecastResponse()
}