package com.andannn.healthconnectdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import com.andannn.healthconnectdemo.ui.theme.HealthConnectDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

private const val TAG = "MainActivity"
val PERMISSIONS =
    setOf(
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
    )
class MainActivity : ComponentActivity() {
    private var api: HealthConnectAPI? = null
    private val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
    private val requestPermissionsLauncher = registerForActivityResult(requestPermissionActivityContract) { granted ->
        if (granted.containsAll(PERMISSIONS)) {
            // Permissions successfully granted
        } else {
            // Lack of required permissions
        }
    }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val myBinder = binder as HealthConnectService.HealthConnectBinder
            api = myBinder.getService()

            lifecycleScope.launch {
                checkPermissionsAndRun()
                api!!.readStepsByTimeRange(
                    startTime = java.time.Instant.now().minus(10, ChronoUnit.DAYS),
                    endTime = java.time.Instant.now()
                )
                while (true) {
                    delay(1000)
//                    api?.foo()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = Intent(this, HealthConnectService::class.java)

        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)


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

        unbindService(serviceConnection)
    }

    private suspend fun checkPermissionsAndRun() {
        val granted = api!!.getGrantedPermissions()
        Log.d(TAG, "checkPermissionsAndRun: granted $granted")
        if (granted.containsAll(PERMISSIONS)) {
            // Permissions already granted; proceed with inserting or reading data
        } else {
            Log.d(TAG, "requestPermissionsLauncher launch: $PERMISSIONS")
            requestPermissionsLauncher.launch(PERMISSIONS)
        }
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
