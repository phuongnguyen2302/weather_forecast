package com.nab.weatherforecast.data

import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.DailyForecastRepository
import com.nab.weatherforecast.domain.DailyForecastResponse
import com.nab.weatherforecast.util.RxScheduler
import io.reactivex.Single

open class DailyForecastRepositoryImpl(private val rxScheduler: RxScheduler) : DailyForecastRepository {
    private val backendRetrofit = BackendRetrofitBuilder

    override fun getDailyForecast(location: String): Single<DailyForecastResponse> {
        return backendRetrofit.weatherApi().getDailyForecast(location)
            .subscribeOn(rxScheduler.io())
            .observeOn(rxScheduler.computation())
            .map { response ->
                if (response.isSuccessful) {

                    val list: DailyForecastListDto? = response.body()
                    val dailyForecastResults: MutableList<DailyForecast> = mutableListOf()
                    if (list != null) {
                        list.dailyList.forEach { forecastItem ->
                            dailyForecastResults.add(
                                forecastItem.toDailyForecast()
                            )
                        }
                    } else {
                        return@map DailyForecastResponse.Empty(response.message())
                    }

                    return@map DailyForecastResponse.Success(dailyForecastResults)
                } else {
                    return@map DailyForecastResponse.Error(response.message())
                }
            }
    }

}