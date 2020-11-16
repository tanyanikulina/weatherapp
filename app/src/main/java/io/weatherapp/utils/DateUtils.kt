package io.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
//    val DF_SERVER_FULL_T = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
//    val DF_SERVER_FULL = "yyyy-MM-dd HH:mm:ss"
//    val DF_SERVER = "yyyy-MM-dd"
//    val DF_SERVER_DOTS = "dd.MM.yyyy"
//    val DF_SHORT_DOT = "dd.MM.yyyy"
//    val DF_SHORT_WORD_Y = "dd MMM yyyy"
//    val DF_MONTH = "MMM yyyy"
//    val DF_MONTH_SHORT = "MM.yyyy"
//    val DF_TIME = "HH:mm"


    val DF_DAY_OF_WEEK = "EEE"
    val DF_WEEK_DAY_MONTH = "EEE, dd MMM"
    val DF_HOUR = "HH"

    fun formatDateFromMillis(millis: Long?, format: String = DF_WEEK_DAY_MONTH): String {
        val d = SimpleDateFormat(format)
        return "" + d.format(if (millis != null) Date(millis) else Date())
    }

}