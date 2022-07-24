package com.nab.weatherforecast.domain

import io.reactivex.Single

class GetDailyForecastUseCase(
    private val dailyForecastRepository: DailyForecastRepository,
) {

    fun execute(location: String): Single<List<DailyForecast>> {
         return dailyForecastRepository.getDailyForecast(location)
            .map { response ->
                when(response) {
                    is DailyForecastResponse.Success -> return@map response.list
                    is DailyForecastResponse.Error -> throw DailyForecastException(response.message)
                    is DailyForecastResponse.Empty -> throw DailyForecastException(response.message)
                }
            }
    }
}