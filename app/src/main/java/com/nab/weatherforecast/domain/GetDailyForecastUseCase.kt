package com.nab.weatherforecast.domain

import io.reactivex.Single

open class GetDailyForecastUseCase(private val dailyForecastRepository: DailyForecastRepository) {
    fun execute(location: String): Single<List<DailyForecast>> {
        if (location.length < 3) return Single.error(DailyForecastInputException)
        return dailyForecastRepository.getDailyForecast(location)
            .map { response ->
                when (response) {
                    is DailyForecastResponse.Success -> return@map response.list
                    is DailyForecastResponse.Error -> throw DailyForecastNetworkException(response.message)
                }
            }
    }
}