package com.healthconnect.healthdata.internal.database.util

import com.andannn.healthdata.internal.database.util.LocalDateTimeConverter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class LocalDateTimeConverterTest {
    @Test
    fun `local date time converter test`() {
        val converter = LocalDateTimeConverter()
        val now = LocalDateTime.now()
        val nowString = converter.toString(now)

        assertEquals(now, converter.fromString(nowString))
    }
}