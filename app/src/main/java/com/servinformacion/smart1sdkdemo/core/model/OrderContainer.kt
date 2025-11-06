package com.servinformacion.smart1sdkdemo.core.model

import com.servinformacion.smart1sdk.android.order.model.Order
import com.servinformacion.smart1sdk.android.route.model.Route

data class OrderContainer(
    val order    : Order,
    val schedule : List<ScheduleContainer>,
    val route    : Route,
)
