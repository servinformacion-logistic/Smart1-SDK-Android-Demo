package com.servinformacion.smart1sdkdemo.core.utils

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.servinformacion.smart1sdk.android.core.model.CoordinatesData
import kotlin.collections.first
import kotlin.collections.forEach

object MapsUtils {
    val DEFAULT_COORDINATES = CoordinatesData(
        latitude = 4.639794,
        longitude = -74.094507,
    )
    const val DEFAULT_ZOOM = 13f
    const val DEFAULT_PADDING = 70
    fun fromCoordinatesDataToLatLng(
        coordinates : CoordinatesData,
    ): LatLng = LatLng(
        coordinates.latitude,
        coordinates.longitude
    )
    fun fromNullCoordinatesDataToLatLng(
        coordinates : CoordinatesData?,
    ): LatLng? = if (coordinates != null) {
        LatLng(
            coordinates.latitude,
            coordinates.longitude
        )
    } else {
        null
    }
    fun fromCoordinatesDataToCameraPosition(
        coordinates : CoordinatesData,
        zoom        : Float = DEFAULT_ZOOM,
    ): CameraPosition = CameraPosition.fromLatLngZoom(
        fromCoordinatesDataToLatLng(coordinates),
        zoom
    )
    fun fromCoordinatesDataToCameraUpdate(
        coordinates : CoordinatesData,
        zoom        : Float = DEFAULT_ZOOM,
    ): CameraUpdate = CameraUpdateFactory.newLatLngZoom(
        fromCoordinatesDataToLatLng(coordinates),
        zoom
    )
    fun fromCoordinatesDataToCameraUpdate(
        coordinates : List<CoordinatesData>,
        padding     : Int = DEFAULT_PADDING,
    ): CameraUpdate {
        if (coordinates.size == 1) {
            return fromCoordinatesDataToCameraUpdate(
                coordinates.first(),
            )
        }
        val latLngBoundsBuilder: LatLngBounds.Builder = LatLngBounds.builder()
        coordinates.forEach {
            latLngBoundsBuilder.include(
                fromCoordinatesDataToLatLng(it)
            )
        }
        return CameraUpdateFactory.newLatLngBounds(
            latLngBoundsBuilder.build(),
            padding
        )
    }
}