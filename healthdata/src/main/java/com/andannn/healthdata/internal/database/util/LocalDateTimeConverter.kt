package com.andannn.healthdata.internal.database.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

internal class LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromString(value: String): LocalDateTime {
        return LocalDateTime.parse(value, formatter)
    }

    @TypeConverter
    fun toString(date: LocalDateTime): String {
        return date.format(formatter)
    }
}

internal fun toLocalDataTime(
    instant: Instant, zoneOffset: ZoneOffset? = null
): LocalDateTime = LocalDateTime.ofInstant(instant, zoneOffset ?: ZoneId.systemDefault())
