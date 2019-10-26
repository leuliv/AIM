package com.ivapps.aduc.helpers

import android.app.Application

class GetTime : Application(){

    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun getTimeAgo(t: Long): String? {
        var time = t
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "just now"
            diff < 2 * MINUTE_MILLIS -> "1 min"
            diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + " min"
            diff < 90 * MINUTE_MILLIS -> "1 hr"
            diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + " hours"
            diff < 48 * HOUR_MILLIS -> "1 day"
            else -> (diff / DAY_MILLIS).toString() + " days"
        }

    }

}