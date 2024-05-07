package com.app.movies.data.utils

import android.os.Handler
import android.os.Looper

fun afterDelay(delayInTime: Long, listener: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        listener.invoke()
    }, delayInTime)
}
