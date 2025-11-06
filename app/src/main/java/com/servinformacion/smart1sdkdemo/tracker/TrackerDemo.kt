package com.servinformacion.smart1sdkdemo.tracker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.servinformacion.smart1sdk.android.core.model.CoordinatesData
import com.servinformacion.smart1sdk.android.transmission.Smart1Tracker
import com.servinformacion.smart1sdk.android.transmission.Smart1TrackerDataProvider
import com.servinformacion.smart1sdk.android.transmission.model.Smart1TrackerConfig
import com.servinformacion.smart1sdk.android.transmission.types.Smart1TrackerLogLevel
import com.servinformacion.smart1sdk.android.transmission.types.Smart1TrackerProcessType
import com.servinformacion.smart1sdkdemo.R
import com.servinformacion.smart1sdkdemo.core.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class TrackerDemo: Service() {
    private val serviceScope = kotlinx.coroutines.GlobalScope
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationRequest             : LocationRequest
    private lateinit var locationCallback            : LocationCallback
    private var currentLocation                      : CoordinatesData? = null // This is just for
    // demo purposes, but we highly recommend to save and retrieve the last location into  a local
    // storage system

    private val notificationManager by lazy {
        getSystemService<NotificationManager>()!!
    }

    override fun onCreate() {
        try {
            super.onCreate()
            initLocationSystem()
        } catch (e: Exception) {
            Unit
        }
    }

    private fun initLocationSystem() {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                TIME_INTERVAL_TO_REQUEST_LOCATION_UPDATES
            ).build()
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    location.lastLocation?.let { nonNullLastLocation ->
                        currentLocation = CoordinatesData(
                            latitude  = nonNullLastLocation.latitude,
                            longitude = nonNullLastLocation.longitude,
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Unit
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        makeTheServiceRemainsInForeground() // Launch the notification for the service
        startAction() // Start the repeatable task(s) of the service
        // By returning START_STICKY we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    private fun startAction() {
        try {
            startLocationUpdates()
            startTracking()
        } catch (e: Exception) {
            Unit
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            Unit
        }
    }

    private fun stopLocationUpdates() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            Unit
        }
    }
    
    private fun startTracking() {
        try {
            Smart1Tracker.start(
                config = Smart1TrackerConfig(
                    reportTrackingTimeIntervalInMillis                   = TIME_INTERVAL_TO_SEND_TRACKER_UPDATES,
                    inOutPortOnActiveOrderValidationTimeIntervalInMillis = TIME_INTERVAL_TO_CHECK_IN_OUT_PORT_TRACKER_UPDATES,
                    skipReportTrackingIfGPSIsDisabled                    = false,
                ),
                provider = object : Smart1TrackerDataProvider {
                    override fun getLastLocation(): CoordinatesData? = currentLocation // We highly recommend to use the last known location
                    override fun getCompanyId(): Long? = null
                },
                coroutineScope = serviceScope,
                onGettingInPort = { portId ->
                    Timber.w("Getting in on the port with id $portId")
                    /* TODO
                    * ️ Important - Data Synchronization:
                    *
                    *  When port entry/exit events are detected, the server may automatically update the schedule of the active order.
                    *  It is strongly recommended to refresh your app's local data (orders, schedules, ports, etc.) when these callbacks are triggered to maintain synchronization with the API.
                    *
                    * Best Practice:
                    *  Implement a single source of truth pattern using a local database.
                    *  When port events trigger data updates, update this centralized store and let reactive observers (e.g., Flow collectors, LiveData) automatically propagate changes to all UI components.
                    * */
                },
                onGettingOutPort = { portId ->
                    Timber.w("Getting out of the port with id $portId")
                    /* TODO
                    * ️ Important - Data Synchronization:
                    *
                    *  When port entry/exit events are detected, the server may automatically update the schedule of the active order.
                    *  It is strongly recommended to refresh your app's local data (orders, schedules, ports, etc.) when these callbacks are triggered to maintain synchronization with the API.
                    *
                    * Best Practice:
                    *  Implement a single source of truth pattern using a local database.
                    *  When port events trigger data updates, update this centralized store and let reactive observers (e.g., Flow collectors, LiveData) automatically propagate changes to all UI components.
                    * */
                },
                onLog = { level, processType, message ->
                    // Optional: Log tracker events
                    val tag = when (processType) {
                        Smart1TrackerProcessType.TRACKING        -> "Smart1Tracker - Tracking"
                        Smart1TrackerProcessType.PORT_VALIDATION -> "Smart1Tracker - In/Out Port Validation"
                        Smart1TrackerProcessType.UNKNOWN         -> "Smart1Tracker - Unknown"
                        null -> "Smart1Tracker"
                    }
                    when (level) {
                        Smart1TrackerLogLevel.INFO -> Timber.i("$tag: $message")
                        Smart1TrackerLogLevel.WARNING -> Timber.w(tag, message)
                        Smart1TrackerLogLevel.ERROR -> Timber.e(tag, message)
                    }
                }
            )
        } catch (e: Exception) { 
            Unit
        }
    }

    private fun stopTracking() {
        try {
            Smart1Tracker.stop()
        } catch (e: Exception) {
            Unit
        }
    }

    private val baseNotification by lazy {
        NotificationCompat.Builder(applicationContext,
            NOTIFICATION_CHANNEL_NAME
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_TEXT)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_NAME,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun makeTheServiceRemainsInForeground() {
        try {
            createNotificationChannel()
            val activityIntent = Intent(applicationContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }
            val notification = baseNotification
                .setContentIntent(pendingIntent)
                .build()
            startForeground(
                NOTIFICATION_CHANNEL_ID,
                notification
            )
        } catch (e: Exception) {
            Unit
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        try {
            stopLocationUpdates()
            stopTracking()
            // You are welcome to handle a restart operation here
            onDestroy()
        } catch (e: Exception) {
            onDestroy()
        }
    }

    override fun onDestroy() {
        try {
            stopLocationUpdates()
            stopTracking()
            // You are welcome to handle a restart operation here
            super.onDestroy()
        } catch (e: Exception) {
            super.onDestroy()
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
    
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = 1001
        private const val NOTIFICATION_CHANNEL_NAME = "Tracker Smart 1 SDK Demo ID"
        private const val NOTIFICATION_TITLE = "The Tracker service is running"
        private const val NOTIFICATION_TEXT = "The Tracker Smart 1 SDK Demo service is running"
        private const val TIME_INTERVAL_TO_REQUEST_LOCATION_UPDATES = 1000L // Change based on your needs
        private const val TIME_INTERVAL_TO_SEND_TRACKER_UPDATES = 20000L // Change based on your needs
        private const val TIME_INTERVAL_TO_CHECK_IN_OUT_PORT_TRACKER_UPDATES = 20000L // Change based on your needs
    }
}