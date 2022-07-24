package com.nab.weatherforecast.data

import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.DailyForecastRepository
import io.reactivex.Single

object DailyForecastRepositoryImpl : DailyForecastRepository {
    private val backendRetrofit = BackendRetrofitBuilder
    private val dailyForecastMapper = DailyForecastMapper

    override fun getDailyForecast(location: String): Single<List<DailyForecast>> {
        return backendRetrofit.weatherApi().getDailyForecast(location).flatMap {
            val dailyForecastList: MutableList<DailyForecast> = mutableListOf()
            it.dailyList.forEach {
                    item -> dailyForecastList.add(dailyForecastMapper.map(item))
            }
            return@flatMap Single.just(dailyForecastList)
        }
    }
}