package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.servinformacion.smart1sdkdemo.R
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer

@Composable
fun OrderItem(
    data     : OrderContainer,
    onClick  : (OrderContainer) -> Unit = {},
    modifier : Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                8.dp
            )
            .clickable { onClick(data) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Order #${data.order.id}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            RightIconText(
                text = "Contains ${data.schedule.size} schedule",
                painter = painterResource(id = R.drawable.ic_calendar),
                iconSize = 16.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}