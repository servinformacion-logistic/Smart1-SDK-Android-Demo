package com.servinformacion.smart1sdkdemo.home

import com.servinformacion.smart1sdk.android.port.model.Port
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer

sealed interface HomeAction {
    data class OnStart(
        val isTrackerRunning : Boolean,
    ) : HomeAction
    data class OnStartOrStopTracker(
        val isTrackerRunning : Boolean,
    ) : HomeAction
    data object OnSettingsClicked  : HomeAction
    data object OnRefreshClicked  : HomeAction
    data object OnSelectOrderClicked : HomeAction
    data class OnOrderSelected(
        val order : OrderContainer,
    )  : HomeAction
    data class OnOrderInfoClicked(
        val order : OrderContainer,
    )  : HomeAction
    data class OnSomePortClicked(
        val port : Port,
    ) : HomeAction
    data object OnSelectOrderDismissed :  HomeAction
    data object OnPortDetailsDismissed :  HomeAction
    data object OnOrderDetailsDismissed :  HomeAction
    data class OnStartOrder(
        val order : OrderContainer,
    ) : HomeAction
}