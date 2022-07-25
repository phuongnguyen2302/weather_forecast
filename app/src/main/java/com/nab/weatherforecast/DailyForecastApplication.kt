package com.nab.weatherforecast

import android.app.Application
import com.nab.weatherforecast.data.DailyForecastRepositoryImpl
import com.nab.weatherforecast.domain.DailyForecastRepository
import com.nab.weatherforecast.domain.GetDailyForecastUseCase
import com.nab.weatherforecast.util.RxScheduler

class DailyForecastApplication : Application() {

    lateinit var rxScheduler: RxScheduler
    lateinit var dailyForecastRepository: DailyForecastRepository
    lateinit var dailyForecastUseCase: GetDailyForecastUseCase

    override fun onCreate() {
        super.onCreate()
        rxScheduler = RxScheduler()
        dailyForecastRepository = DailyForecastRepositoryImpl(rxScheduler)
        dailyForecastUseCase = GetDailyForecastUseCase(dailyForecastRepository)
    }
}