package com.andannn.healthconnectdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import com.andannn.healthconnectdemo.ui.theme.HealthConnectDemoTheme
import com.andannn.healthdata.DataSyncHelper.registerSyncScheduleWorker
import com.andannn.healthdata.HealthRepositoryProvider
import kotlinx.coroutines.launch
import java.time.Instant

private const val TAG = "MainActivity"
val PERMISSIONS =
    setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
    )

class MainActivity : ComponentActivity() {
    private val requestPermissionActivityContract =
        PermissionController.createRequestPermissionResultContract()
    private val requestPermissionsLauncher =
        registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions successfully granted
            } else {
                // Lack of required permissions
            }
        }

    private val repository by lazy {
        (application as HealthRepositoryProvider).repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            registerSyncScheduleWorker(application)
        }

        setContent {
            HealthConnectDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(modifier = Modifier.padding(innerPadding)) {
                        var selected by remember {
                            mutableStateOf(Type.STEPS)
                        }
                        ScrollableTabRow(selectedTabIndex = Type.entries.indexOf(selected)) {
                            Type.entries.forEach {
                                Tab(selected = it == selected,
                                    onClick = {
                                        selected = it
                                    }
                                ) {
                                    Text(text = it.name)
                                }
                            }
                        }

                        val weights = when (selected) {
                            Type.STEPS -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getSteps(
                                            Instant.now().minusSeconds(3600 * 12),
                                            Instant.now(),
                                        ).map { it.toString() }
                                    }
                                }
                            }

                            Type.HEIGHT -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value =
                                            repository.getHeights(
                                                Instant.now().minusSeconds(3600 * 12),
                                                Instant.now(),
                                            ).map { it.toString() }
                                    }
                                }
                            }

                            Type.WEIGHT -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getWeights(
                                            Instant.now().minusSeconds(3600 * 12),
                                            Instant.now(),
                                        ).map { it.toString() }
                                    }
                                }
                            }

                            Type.SLEEP -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getSleeps(
                                            Instant.now().minusSeconds(3600 * 12),
                                            Instant.now(),
                                        ).map { it.toString() }
                                    }
                                }
                            }

                            Type.SPEED -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getSpeeds(
                                            Instant.now().minusSeconds(3600 * 12),
                                            Instant.now(),
                                        ).map { it.toString() }
                                    }
                                }
                            }

                            Type.DISTANCE -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getDistance(
                                            Instant.now().minusSeconds(3600 * 12),
                                            Instant.now(),
                                        ).map { it.toString() }
                                    }
                                }
                            }

                            Type.CALORIES -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getTotalCaloriesBurned(
                                            Instant.now().minusSeconds(3600 * 12),
                                            Instant.now(),
                                        ).map { it.toString() }
                                    }
                                }
                            }

                            Type.BODY_DATA -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = listOf(
                                            repository.getLatestBodyMeasurementData().toString()
                                        )
                                    }
                                }
                            }

                            Type.AGGRAVATED_DATA -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = listOf(
                                            repository.getAggregateHealthData(
                                                Instant.now().minusSeconds(3600 * 12),
                                                Instant.now(),
                                            ).toString()
                                        )
                                    }
                                }
                            }
                        }

                        LazyColumn {
                            items(weights.value, key = { it }) {
                                Text(text = it)

                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class Type {
    AGGRAVATED_DATA,
    BODY_DATA,
    STEPS,
    HEIGHT,
    WEIGHT,
    SLEEP,
    SPEED,
    DISTANCE,
    CALORIES,
}
