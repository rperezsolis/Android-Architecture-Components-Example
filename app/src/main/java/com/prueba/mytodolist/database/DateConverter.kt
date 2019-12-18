package com.prueba.mytodolist.database

import androidx.room.TypeConverter
import java.util.*

//We use converters, so we can store non primitive objects in our database.

class DateConverter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun toDate(timestamp: Long): Date {
            return Date(timestamp)
        }

        @TypeConverter
        @JvmStatic
        fun toTimestamp(date: Date): Long {
            return date.time
        }
    }
}