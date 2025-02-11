package com.andannn.healthconnectdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import com.andannn.healthconnectdemo.ui.theme.HealthConnectDemoTheme
import com.andannn.healthdata.DataSyncHelper.registerSyncScheduleWorker
import com.andannn.healthdata.HealthRepositoryProvider
import kotlinx.coroutines.launch

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

//    private val serviceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
//            val myBinder = binder as HealthConnectService.HealthConnectBinder
//            api = myBinder.getService()
//
//            lifecycleScope.launch {
//                checkPermissionsAndRun()
//                api!!.foo()
//                api!!.readStepsByTimeRange(
//                    startTime = java.time.Instant.now().minus(1, ChronoUnit.HOURS),
//                    endTime = java.time.Instant.now()
//                )
//                while (true) {
//                    delay(1000)
////                    api?.foo()
//                }
//            }
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val intent = Intent(this, HealthConnectService::class.java)
//
//        startService(intent)
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)


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

                        val weights = when(selected)  {
                            Type.STEPS -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getSteps()
                                    }
                                }
                            }
                            Type.HEIGHT -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getHeights()
                                    }
                                }
                            }
                            Type.WEIGHT -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getWeights()
                                    }
                                }
                            }
                            Type.SLEEP -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getSleeps()
                                    }
                                }
                            }
                            Type.SPEED -> {
                                produceState(emptyList<String>()) {
                                    launch {
                                        value = repository.getSpeeds()
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            weights.value.forEach {
                                Text(text = it)

                                HorizontalDivider()
                            }

                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

//        unbindService(serviceConnection)
    }

    private suspend fun checkPermissionsAndRun() {
//        Log.d(TAG, "checkPermissionsAndRun: granted $granted")
//        if (granted.containsAll(PERMISSIONS)) {
//            // Permissions already granted; proceed with inserting or reading data
//        } else {
//            Log.d(TAG, "requestPermissionsLauncher launch: $PERMISSIONS")
//            requestPermissionsLauncher.launch(PERMISSIONS)
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthConnectDemoTheme {
        Greeting("Android")
    }
}

enum class Type {
    STEPS,
    HEIGHT,
    WEIGHT,
    SLEEP,
    SPEED,
}
