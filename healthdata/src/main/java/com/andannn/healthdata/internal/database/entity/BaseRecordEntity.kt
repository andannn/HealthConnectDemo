package com.andannn.healthdata.internal.database.entity

internal object BaseColumn {
    const val ID = "step_record_id"
    const val DATA_ORIGIN_PACKAGE_NAME = "data_origin_package_name"
    const val LAST_MODIFIED_TIME = "last_modified_time"
    const val START_TIME = "start_time"
    const val END_TIME = "end_time"
    const val RECORD_TIME = "record_time"
}

internal interface BaseRecordEntity {
    val id: String

    val dataOriginPackageName: String

    val lastModifiedTime: Long
}

internal interface IntervalRecordEntity {
    val startTime: Long
    val endTime: Long
}

internal interface InstantaneousRecordEntity {
    public val time: Long
}