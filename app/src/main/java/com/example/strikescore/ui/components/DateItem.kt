package com.example.strikescore.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class DateItem(
    val id: Long,  // Unique identifier, can be the epoch day or something similar
    val dayOfWeek: String,
    val dayOfMonth: Int
)

@RequiresApi(Build.VERSION_CODES.O)
fun generateDateItems(selectedDate: LocalDate): List<DateItem> {
    val items = mutableListOf<DateItem>()
    val startOfMonth = selectedDate.withDayOfMonth(1)
    val endOfMonth = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth())

    var currentDate = startOfMonth
    while (currentDate.isBefore(endOfMonth) || currentDate.isEqual(endOfMonth)) {
        items.add(
            DateItem(
                id = currentDate.toEpochDay(),  // Example identifier
                dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                dayOfMonth = currentDate.dayOfMonth
            )
        )
        currentDate = currentDate.plusDays(1)
    }

    return items
}