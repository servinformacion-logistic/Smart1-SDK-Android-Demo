package com.servinformacion.smart1sdkdemo

import android.app.Application
import com.servinformacion.smart1sdk.android.core.utils.EM
import com.servinformacion.smart1sdk.android.init_config.InitSDKConfig
import com.servinformacion.smart1sdk.android.init_config.Smart1SDK
import com.servinformacion.smart1sdkdemo.di.viewModelDiModule
import timber.log.Timber

class Smart1SDKDemoApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {
        Timber.plant(Timber.DebugTree())
        Smart1SDK.initialize(
            context = this,
            addKoinAndroidLogger = true,
            extraKoinModules = listOf(
                viewModelDiModule,
            )
        )
        EM().i(0)
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