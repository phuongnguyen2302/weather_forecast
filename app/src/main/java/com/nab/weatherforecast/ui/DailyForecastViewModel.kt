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
    val errorText = MutableLiveData("")
    val inputText = MutableLiveData("")

    private val _dailyForecastList = MutableLiveData<List<DailyForecastItemViewModel>>(emptyList())
    val dailyForecastList: LiveData<List<DailyForecastItemViewModel>> = _dailyForecastList

    fun onPause() {
        disposable.clear()
    }

    fun onGetWeatherClick() {
        disposable.add(
            dailyForecastUseCase
                .execute(inputText.value.orEmpty())
                .observeOn(rxScheduler.androidMainThread())
                .subscribe({ response ->
                    _dailyForecastList.value = response.map { toItemViewModel(it) }
                }, { error ->
                    error.printStackTrace()
                    errorText.value = "An error is occurred. Please try again later!"
                })
        )
    }

    fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        inputText.value = text.toString()
    }

    private fun toItemViewModel(dailyForecast: DailyForecast): DailyForecastItemViewModel {
        return DailyForecastItemViewModel(
            date = dailyForecast.date,
            temperature = "${dailyForecast.temperature}˚C",
            pressure = "${dailyForecast.pressure}",
            humidity = "${dailyForecast.humidity}%",
            weatherDescription = dailyForecast.weatherDescription,
        )
    }
}