package com.example.strikescore.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Data class representing an item containing information about a specific date.
 *
 * @param id Unique identifier for the date item (e.g., epoch day or similar).
 * @param dayOfWeek The day of the week as a string.
 * @param dayOfMonth The day of the month as an integer.
 */
data class DateItem(
    val id: Long,  // Unique identifier, can be the epoch day or something similar
    val dayOfWeek: String,
    val dayOfMonth: Int
)
/**
 * Generates a list of [DateItem] objects representing the days of the month for a given [selectedDate].
 *
 * @param selectedDate The selected date from which to generate the date items.
 * @return A list of [DateItem] objects representing the days of the month.
 */
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