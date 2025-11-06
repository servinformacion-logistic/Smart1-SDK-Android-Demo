package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RightIconText(
    text               : String,
    painter            : Painter,
    contentDescription : String? = null,
    textColor          : Color = MaterialTheme.colorScheme.onBackground,
    textStyle          : TextStyle = MaterialTheme.typography.bodySmall,
    iconColor          : Color? = MaterialTheme.colorScheme.onBackground,
    iconSize           : Dp = 24.dp,
    modifier           : Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            colorFilter = if (iconColor != null) { ColorFilter.tint(color = iconColor) } else { null },
            modifier = Modifier
                .size(iconSize),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun RightIconText(
    text               : String,
    imageVector        : ImageVector,
    contentDescription : String? = null,
    textColor          : Color = MaterialTheme.colorScheme.onBackground,
    textStyle          : TextStyle = MaterialTheme.typography.bodySmall,
    iconColor          : Color? = MaterialTheme.colorScheme.onBackground,
    iconSize           : Dp = 24.dp,
    modifier           : Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            imageVector = imageVector,
            contentDescription = contentDescription,
            colorFilter = if (iconColor != null) { ColorFilter.tint(color = iconColor) } else { null },
            modifier = Modifier
                .size(iconSize),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}