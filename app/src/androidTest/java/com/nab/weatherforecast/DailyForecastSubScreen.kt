package com.nab.weatherforecast

import com.kaspersky.kaspresso.screens.KScreen
import com.nab.weatherforecast.ui.DailyForecastFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

class DailyForecastSubScreen : KScreen<DailyForecastSubScreen>() {
    override val layoutId = R.layout.fragment_daily_forecast
    override val viewClass = DailyForecastFragment::class.java

    val recyclerView = KRecyclerView(
        builder = { withId(R.id.daily_forecast_recycler_view) },
        itemTypeBuilder = {
            itemType(::DailyForecastItem)
        }
    )

    val inputText = KEditText { withId(R.id.edit_text_location) }
    val getWeatherButton = KButton { withId(R.id.button_get_weather) }
    val errorText = KTextView { withId(R.id.text_view_error_message) }
}