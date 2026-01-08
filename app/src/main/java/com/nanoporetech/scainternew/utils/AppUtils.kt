package com.nanoporetech.scainternew.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun displayedDateAndTime(date: String): String {
    return try {
        val parsed = OffsetDateTime.parse(date)
        formatDate(parsed, timeStyle = FormatStyle.SHORT)
    } catch (e: Exception) {
        "Unknown date"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(
    date: OffsetDateTime,
    dateStyle: FormatStyle = FormatStyle.MEDIUM,
    timeStyle: FormatStyle? = null
): String {
    val formatter = if (timeStyle != null) {
        DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
    } else {
        DateTimeFormatter.ofLocalizedDate(dateStyle)
    }.withLocale(Locale.getDefault())

    return formatter.format(date)
}