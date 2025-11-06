package com.servinformacion.smart1sdkdemo.home

import com.servinformacion.smart1sdk.android.core.model.CoordinatesData
import com.servinformacion.smart1sdk.android.port.model.Port
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer

data class HomeState(
    val isLoading             : Boolean = false,
    val currentLoadingMsg     : String? = null,
    val isTrackerEnabled      : Boolean = false,
    val orders                : List<OrderContainer> = emptyList(),
    val isShowingOrderPicker  : Boolean = false,
    val isShowingPortDetails  : Boolean = false,
    val isShowingOrderDetails : Boolean = false,
    val selectedOrder         : OrderContainer? = null,
    val selectedPort          : Port? = null,
    val polylineCoordinates   : List<List<CoordinatesData>> = emptyList(),
    val ports                 : List<Port> = emptyList(),
)