package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SelectableItem(
    text            : String,
    onClick         : () -> Unit,
    textColor       : Color = MaterialTheme.colorScheme.surface,
    textStyle       : TextStyle = MaterialTheme.typography.bodyMedium,
    textFontWeight  : FontWeight = FontWeight.SemiBold,
    backgroundColor : Color= MaterialTheme.colorScheme.secondary,
    modifier        : Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable {
                onClick()
            }
            .padding(8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = text,
            color = textColor,
            style = textStyle,
            fontWeight = textFontWeight,
            textAlign = TextAlign.Center,
            modifier = Modifier

        )
        Spacer(modifier = Modifier.size(4.dp))
        Image(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Select an option",
            colorFilter = ColorFilter.tint(color = textColor),
            modifier = Modifier
                .size(24.dp),
        )
    }
}