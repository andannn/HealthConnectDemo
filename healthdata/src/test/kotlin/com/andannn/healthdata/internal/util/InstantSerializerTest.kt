package com.andannn.healthdata.internal.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant

class InstantSerializerTest {
    private val json = Json {
        serializersModule = SerializersModule {
            InstantSerializerModuleBuilder()
        }
    }

    @Test
    fun `InstantSerializer test`() {
        assertEquals(
            "\"2025-02-19T01:00:21.856Z\"",
            json.encodeToString<Instant>(
                Instant.ofEpochMilli(1739926821856)
            )
        )

        assertEquals(
            Instant.ofEpochMilli(1739926821856),
            json.decodeFromString<Instant>(
                "\"2025-02-19T01:00:21.856Z\"",
            )
        )
    }
}