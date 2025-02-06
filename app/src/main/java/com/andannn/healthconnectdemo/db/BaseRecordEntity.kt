package com.andannn.healthconnectdemo.db

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