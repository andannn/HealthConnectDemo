package com.andannn.healthdata.internal.database

import java.time.LocalDateTime

interface BaseRecordEntity {
    val id: String

    val dataOriginPackageName: String

    val lastModifiedTime: LocalDateTime
}

internal interface IntervalRecordEntity {
    val startTime: LocalDateTime
    val endTime: LocalDateTime
}