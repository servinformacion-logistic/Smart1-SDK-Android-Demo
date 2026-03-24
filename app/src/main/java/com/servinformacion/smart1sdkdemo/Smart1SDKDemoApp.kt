package com.servinformacion.smart1sdkdemo

import android.app.Application
import com.servinformacion.smart1sdk.android.init_config.Smart1SDK
import com.servinformacion.smart1sdkdemo.core.utils.StartSDKUtils
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
        StartSDKUtils.startSDK()
    }
}