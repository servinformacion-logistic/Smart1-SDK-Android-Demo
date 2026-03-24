package com.servinformacion.smart1sdkdemo.core.utils

import com.servinformacion.smart1sdk.android.init_config.InitSDKConfig
import com.servinformacion.smart1sdkdemo.BuildConfig

object StartSDKUtils {
    fun startSDK() {
        if (
            BuildConfig.smart1SDKApiKey.isEmpty() ||
            BuildConfig.smart1SDKUserOperatorEmail.isEmpty()
        ) {
            throw Exception(
                """
                    The SDK API Key or/and the User Operator Email is empty.
                    
                    - SDK API Key: ${BuildConfig.smart1SDKApiKey}
                    - User Operator Email: ${BuildConfig.smart1SDKUserOperatorEmail}
                    
                    Please check the values in the local.properties file.
                """.trimIndent()
            )
        }
        val initSDKConfig = InitSDKConfig()
        if (!initSDKConfig.isInitialized()) {
            initSDKConfig.init(
                sdkApiKey = BuildConfig.smart1SDKApiKey,
                email     = BuildConfig.smart1SDKUserOperatorEmail,
            )
        }
    }
}