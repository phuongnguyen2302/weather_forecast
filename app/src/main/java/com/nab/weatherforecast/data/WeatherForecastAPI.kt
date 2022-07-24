package com.nab.weatherforecast.data

import com.nab.weatherforecast.BuildConfig
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastAPI {
    @GET(BuildConfig.WEATHER_FORECAST_PATH_AND_QUERY)
    fun getDailyForecast(
        @Query(value = "q") time: String,
        @Query(value = "cnt") numberOfDays: Int = 7,
        @Query(value = "units") units: String = "metric",
        @Query(value = "appid") appid: String = BuildConfig.WEATHER_FORECASE_API_KEY,
    ): Single<Response<DailyForecastListDto>>
}