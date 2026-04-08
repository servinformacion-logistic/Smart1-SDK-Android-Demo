package com.servinformacion.smart1sdkdemo.home

import com.servinformacion.smart1sdk.android.core.model.CoordinatesData

sealed interface HomeEvent {
    data object StartTracker : HomeEvent
    data object NotifyNewOrderInProgressToTracker : HomeEvent
    data object StopTracker : HomeEvent
    data object GoToAppSettings : HomeEvent
    data class OpenCloseOrderPicker(
        val shouldOpen : Boolean = false,
    ) : HomeEvent
    data class OpenCloseOrderDetails(
        val shouldOpen : Boolean = false,
    ) : HomeEvent
    data class OpenClosePortDetails(
        val shouldOpen : Boolean = false,
    ) : HomeEvent
    data class UpdateMapCamera(
        val coordinates : List<CoordinatesData>
    ): HomeEvent
    data class Error(
        val msg : String
    ) : HomeEvent
}