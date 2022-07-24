package com.nab.weatherforecast.domain

import io.reactivex.Single

class GetDailyForecastUseCase(private val dailyForecastRepository: DailyForecastRepository) {

    fun execute(location: String): Single<List<DailyForecast>> {
        return dailyForecastRepository.getDailyForecast(location)
    }
}