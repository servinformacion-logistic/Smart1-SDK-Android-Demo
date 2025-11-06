package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogStructure(
    isOpen    : Boolean,
    onDismiss : () -> Unit,
    backgroundColor : Color = MaterialTheme.colorScheme.surface,
    modifier  : Modifier = Modifier,
    content   : @Composable () -> Unit,
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Column(
                modifier = modifier
                    .shadow(
                        8.dp, shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        backgroundColor,
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }
    }
}