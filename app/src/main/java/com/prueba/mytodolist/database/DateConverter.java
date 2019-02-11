package com.prueba.mytodolist.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    //We use converters, so we can store non primitive objects in our database.

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}