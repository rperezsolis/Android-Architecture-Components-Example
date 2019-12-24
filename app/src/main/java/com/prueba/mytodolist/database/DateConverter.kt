package com.prueba.mytodolist.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

//We use converters, so we can store non primitive objects in our database.

class DateConverter {

    companion object {

        private val DATE_FORMAT: String = "EEE, dd-MM-YYYY, HH:mm aaa"
        val dateFormat: SimpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        @TypeConverter
        @JvmStatic
        fun timestampToDate(timestamp: Long?): Date? {
            return when(timestamp) {
                null -> null
                else -> Date(timestamp)
            }
        }

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): Long? {
            return when (date){
                null -> null
                else -> date.time
            }
        }
    }
}