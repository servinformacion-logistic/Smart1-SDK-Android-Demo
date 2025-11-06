package com.servinformacion.smart1sdkdemo.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object IntentUtils {
    fun goToLocationSourceSettings(
        context : Context
    ) {
        try {
            val intent = Intent().apply {
                action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
            context.startActivity(intent)
        } catch (e: Exception) { Unit }
    }

    fun goToAppDetailsSettings(
        context : Context
    ) {
        try {
            // Creating the intent object
            val i: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.data = Uri.parse("package:${context.packageName}")
            // Start the intent
            context.startActivity(i)
        } catch (e: Exception) { Unit }
    }
}