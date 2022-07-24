package com.nab.weatherforecast.domain

import io.reactivex.Single

interface DailyForecastRepository {
    fun getDailyForecast(location: String): Single<DailyForecastResponse>
}