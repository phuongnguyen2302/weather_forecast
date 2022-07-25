package com.nab.weatherforecast

import android.view.View
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

class DailyForecastItem(parent: Matcher<View>) : KRecyclerItem<DailyForecastItem>(parent) {
    val date = KTextView { withId(R.id.text_view_date) }
    val averageTemperature = KTextView { withId(R.id.text_view_average_temperature) }
    val pressure = KTextView { withId(R.id.text_view_pressure) }
    val humidity = KTextView { withId(R.id.text_view_humidity) }
    val description = KTextView { withId(R.id.text_view_description) }
}