package com.servinformacion.smart1sdkdemo.core.model

import com.servinformacion.smart1sdk.android.dock.model.Dock
import com.servinformacion.smart1sdk.android.port.model.Port
import com.servinformacion.smart1sdk.android.schedule.model.Schedule

data class ScheduleContainer (
    val schedule : Schedule,
    val port     : Port,
    val dock     : Dock,
)