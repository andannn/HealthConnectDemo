package com.andannn.healthconnectdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import com.andannn.healthconnectdemo.ui.theme.HealthConnectDemoTheme
import com.andannn.healthdata.DataSyncHelper.registerSyncScheduleWorker
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
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
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
