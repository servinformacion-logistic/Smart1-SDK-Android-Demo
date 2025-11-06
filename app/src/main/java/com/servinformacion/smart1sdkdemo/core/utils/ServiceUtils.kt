package com.servinformacion.smart1sdkdemo.core.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import kotlin.also

object ServiceUtils {

    fun isServiceRunning(
        serviceClass : Class<*>,
        context      : Context,
    ): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun startForegroundService(
        serviceClass : Class<*>,
        context      : Context,
    ) {
        if (!isServiceRunning(serviceClass, context)) {
            Intent(context, serviceClass).also { intent ->
                context.startForegroundService(intent)
            }
        }
    }

    fun stopService(
        serviceClass : Class<*>,
        context      : Context,
    ) {
        if (isServiceRunning(serviceClass, context)) {
            Intent(context, serviceClass).also { intent ->
                context.stopService(intent)
            }
        }
    }
}