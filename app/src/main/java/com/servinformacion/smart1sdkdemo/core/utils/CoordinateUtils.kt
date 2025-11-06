package com.servinformacion.smart1sdkdemo.core.utils

import com.servinformacion.smart1sdk.android.core.model.CoordinatesData

object CoordinateUtils {
    fun decodePolyline(
        encodedPolyline    : String,
        inverseCoordinates : Boolean = false
    ): List<CoordinatesData> {
        return try {
            if (encodedPolyline.isEmpty()) {
                return emptyList()
            }
            val coordinates = mutableListOf<CoordinatesData>()
            var index = 0
            var lat = 0
            var lng = 0

            while (index < encodedPolyline.length) {
                var b: Int
                var shift = 0
                var result = 0

                do {
                    b = encodedPolyline[index].toInt() - 63
                    index++
                    result = result or ((b and 0x1f) shl shift)
                    shift += 5
                } while (b >= 0x20)

                val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dlat

                shift = 0
                result = 0

                do {
                    b = encodedPolyline[index].toInt() - 63
                    index++
                    result = result or ((b and 0x1f) shl shift)
                    shift += 5
                } while (b >= 0x20)

                val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lng += dlng

                coordinates.add(
                    if (inverseCoordinates) {
                        CoordinatesData(lng / 1e5, lat / 1e5)
                    } else {
                        CoordinatesData(lat / 1e5, lng / 1e5)
                    }
                )
            }

            return coordinates
        } catch (e: Exception) { emptyList() }
    }
}