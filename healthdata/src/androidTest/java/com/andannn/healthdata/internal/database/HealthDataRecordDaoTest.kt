package com.andannn.healthdata.internal.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andannn.healthdata.internal.database.dao.HealthDataRecordDao
import com.andannn.healthdata.internal.database.entity.StepsRecordEntity
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest


@RunWith(AndroidJUnit4::class)
class HealthDataRecordDaoTest {
    private lateinit var db: HealthDataRecordDatabase
    private lateinit var dao: HealthDataRecordDao
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HealthDataRecordDatabase::class.java
        ).build()
        dao = db.getHealthDataRecordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val stepsRecord = StepsRecordEntity(
        id = "1",
        dataOriginPackageName = "com.google.android.apps.health",
        lastModifiedTime = LocalDateTime.now(),
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now(),
        count = 1000
    )
    private val newStepsRecord = StepsRecordEntity(
        id = "1",
        dataOriginPackageName = "com.google.android.apps.health",
        lastModifiedTime = LocalDateTime.now(),
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now(),
        count = 1001
    )



    @Test
    fun upsertStepRecordTest() = testScope.runTest {
        dao.upsertStepRecords(listOf(stepsRecord))

        val result = dao.getStepRecords()
        assertEquals(1, result.size)
        assertEquals(stepsRecord, result[0])
    }

    @Test
    fun updateStepRecordTest() = testScope.runTest {
        dao.upsertStepRecords(listOf(stepsRecord))

        dao.upsertStepRecords(listOf(newStepsRecord))

        val result = dao.getStepRecords()
        assertEquals(1, result.size)
        assertEquals(newStepsRecord, result[0])
    }

    @Test
    fun deleteStepRecordTest() = testScope.runTest {
        dao.upsertStepRecords(listOf(stepsRecord))

        dao.deleteStepRecordsByIds(listOf(stepsRecord.id))

        val result = dao.getStepRecords()
        assertEquals(0, result.size)
    }
}