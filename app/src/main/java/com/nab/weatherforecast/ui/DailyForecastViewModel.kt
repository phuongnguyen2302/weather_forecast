package com.nab.weatherforecast.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.DailyForecastInputException
import com.nab.weatherforecast.domain.DailyForecastNetworkException
import com.nab.weatherforecast.domain.GetDailyForecastUseCase
import com.nab.weatherforecast.util.RxScheduler
import io.reactivex.disposables.CompositeDisposable


class DailyForecastViewModel(
    private val dailyForecastUseCase: GetDailyForecastUseCase,
    private val rxScheduler: RxScheduler
) : ViewModel() {

    val disposable = CompositeDisposable()
    val errorText = ObservableField("")
    val inputText = ObservableField("")

    private val _dailyForecastList = MutableLiveData<List<DailyForecastItemViewModel>>(emptyList())
    val dailyForecastList: LiveData<List<DailyForecastItemViewModel>> = _dailyForecastList

    fun onPause() {
        disposable.clear()
    }

    fun onGetWeatherClick() {
        reset()
        disposable.add(
            dailyForecastUseCase
                .execute(inputText.get().orEmpty())
                .observeOn(rxScheduler.androidMainThread())
                .subscribe({ response ->
                    _dailyForecastList.value = response.map { toItemViewModel(it) }
                }, { error ->
                    error.printStackTrace()
                    when (error) {
                        is DailyForecastNetworkException -> errorText.set("An error is occurred. Please check your connection and try again!")
                        is DailyForecastInputException -> errorText.set("Location name must have 3 characters and above. Please try again!")
                    }
                })
        )
    }

    private fun reset() {
        _dailyForecastList.value = emptyList()
        errorText.set("")
    }

    fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        reset()
        inputText.set(text.toString())
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