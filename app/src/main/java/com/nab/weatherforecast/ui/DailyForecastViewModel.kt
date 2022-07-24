package com.nab.weatherforecast.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.GetDailyForecastUseCase
import com.nab.weatherforecast.util.RxScheduler
import io.reactivex.disposables.CompositeDisposable


class DailyForecastViewModel(
    private val dailyForecastUseCase: GetDailyForecastUseCase,
    private val rxScheduler: RxScheduler
) : ViewModel() {

    val disposable = CompositeDisposable()

    val inputText = MutableLiveData("")
    val errorText = MutableLiveData("")

    private val _dailyForecastList = MutableLiveData<List<DailyForecast>>(emptyList())
    val dailyForecastList: LiveData<List<DailyForecast>> = _dailyForecastList

    fun onPause() {
        disposable.clear()
    }

    fun onGetWeatherClick() {
        disposable.add(
            dailyForecastUseCase
                .execute(inputText.value.orEmpty())
                .observeOn(rxScheduler.androidMainThread())
                .subscribe({ response ->
                    _dailyForecastList.value = response
                }, { error ->
                    error.printStackTrace()
                    errorText.value = "An error is occurred. Please try again later!"
                })
        )
    }
}