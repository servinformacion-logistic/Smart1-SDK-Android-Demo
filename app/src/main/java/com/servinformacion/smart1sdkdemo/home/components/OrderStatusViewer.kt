package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.servinformacion.smart1sdk.android.order.types.OrderState
import com.servinformacion.smart1sdkdemo.R

@Composable
fun OrderStatusViewer(
    orderState : OrderState,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .then(
                if (orderState == OrderState.ASSIGNED) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(8.dp))
            .background(
                when (orderState) {
                    OrderState.ASSIGNED     -> Color.Blue
                    OrderState.IN_PROGRESS  -> Color.Magenta
                    OrderState.COMPLETED    -> Color.Green
                    else                    -> Color.Gray
                }
            )
            .padding(
                8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (orderState == OrderState.ASSIGNED) {
            Image(
                painter = painterResource(id = R.drawable.ic_start),
                contentDescription = "Start",
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onClick() },
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = if (orderState == OrderState.ASSIGNED) { "START" } else { orderState.name},
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
    }
}