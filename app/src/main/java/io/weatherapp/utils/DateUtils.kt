package io.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

const val DF_DAY_OF_WEEK = "EEE"
const val DF_WEEK_DAY_MONTH = "EEE, dd MMM"
const val DF_HOUR = "HH"

object DateUtils {


    fun formatDateFromMillis(millis: Long?, format: String = DF_WEEK_DAY_MONTH): String {
        val d = SimpleDateFormat(format)
        return "" + d.format(if (millis != null) Date(millis) else Date())
    }

}