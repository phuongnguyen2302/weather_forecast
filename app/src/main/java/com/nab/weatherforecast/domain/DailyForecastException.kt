package com.nab.weatherforecast.domain

import java.lang.Exception

open class DailyForecastException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Exception(message ?: "Error message missing", cause)