package com.andannn.healthdata.internal.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModuleBuilder
import java.time.Instant

internal val InstantSerializerModuleBuilder: SerializersModuleBuilder.() -> Unit = {
    contextual(Instant::class, InstantSerializer)
}

private object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        return Instant.parse(string)
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        val string = value.toString()
        encoder.encodeString(string)
    }
}