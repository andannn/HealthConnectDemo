//package com.andannn.healthconnectdemo
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
//import android.os.Binder
//import android.os.IBinder
//import android.util.Log
//import androidx.core.app.NotificationCompat
//
//private const val TAG = "HealthConnectService"
//
//class HealthConnectService : Service() {
//    private lateinit var healthConnectAPI: HealthConnectAPI
//
//    private val binder = HealthConnectBinder()
//
//    override fun onBind(intent: Intent): IBinder {
//        return binder
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        healthConnectAPI = HealthConnectAPIImpl(this)
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d(TAG, "onStartCommand: intent $intent flags $flags, startId: $startId")
//        val channel = NotificationChannel(
//            "CHANNEL_ID",
//            "Foreground Service Channel",
//            NotificationManager.IMPORTANCE_DEFAULT
//        )
//        val notificationManager: NotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//        val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
//            .setContentTitle("Service is running")
//            .setContentText("Your service is still active")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .build()
//
//        startForeground(1, notification, FOREGROUND_SERVICE_TYPE_HEALTH)
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "onDestroy: ")
//    }
//
//    inner class HealthConnectBinder : Binder() {
//        fun getService(): HealthConnectAPI {
//            return healthConnectAPI
//        }
//    }
//}
