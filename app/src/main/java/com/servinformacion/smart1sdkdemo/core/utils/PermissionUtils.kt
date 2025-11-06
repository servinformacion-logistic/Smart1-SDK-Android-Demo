package com.servinformacion.smart1sdkdemo.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun isAccessFineLocationPermissionGranted(
        context : Context,
    ) : Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            .equals(PackageManager.PERMISSION_GRANTED)
    fun isAccessCoarseLocationPermissionGranted(
        context : Context,
    ) : Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            .equals(PackageManager.PERMISSION_GRANTED)
    @RequiresApi(Build.VERSION_CODES.Q)
    fun isAccessBackgroundLocationPermissionGranted(
        context : Context,
    ) : Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            .equals(PackageManager.PERMISSION_GRANTED)

    fun isAllLocationPermissionsGranted(
        context : Context,
    ) : Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isAccessCoarseLocationPermissionGranted(context) &&
        isAccessFineLocationPermissionGranted(context) &&
        isAccessBackgroundLocationPermissionGranted(context)
    } else {
        isAccessCoarseLocationPermissionGranted(context) &&
        isAccessFineLocationPermissionGranted(context)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun isPostNotificationPermissionGranted(
        context : Context,
    ) : Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            .equals(PackageManager.PERMISSION_GRANTED)

    fun getGeneralPermissionsToRequest(
        context : Context,
    ) : List<String> {
        val permissions = mutableListOf<String>()
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !isPostNotificationPermissionGranted(context)
        ) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (
            !isAccessFineLocationPermissionGranted(context)
        ) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (
            !isAccessCoarseLocationPermissionGranted(context)
        ) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return permissions
    }
}