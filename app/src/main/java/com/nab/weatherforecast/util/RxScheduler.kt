package com.nab.weatherforecast.util

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class RxScheduler {
    open fun androidMainThread() = AndroidSchedulers.mainThread()

    open fun computation() = Schedulers.computation()

    open fun io() = Schedulers.io()
}