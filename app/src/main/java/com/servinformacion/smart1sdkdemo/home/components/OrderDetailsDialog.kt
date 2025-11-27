@file:OptIn(ExperimentalMaterial3Api::class)

package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.servinformacion.smart1sdk.android.route.model.Route
import com.servinformacion.smart1sdkdemo.R
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer

@Composable
fun OrderDetailsDialog(
    isOpen           : Boolean,
    data             : OrderContainer,
    onStateClick     : (OrderContainer) -> Unit = {},
    onDismiss        : () -> Unit,
    bottomSheetState : SheetState = rememberModalBottomSheetState(),
    modifier         : Modifier = Modifier
) {
    if (isOpen) {
        ModalBottomSheet(
            modifier = modifier
                .statusBarsPadding(),
            sheetState = bottomSheetState,
            onDismissRequest = onDismiss,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "Order #${data.order.id}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(
                        modifier = Modifier.height(
                            8.dp
                        )
                    )
                }
                item {
                    OrderStatusViewer(
                        orderState = data.order.state,
                        onClick = { onStateClick(data) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    vertical = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(0.3f))
                                    .height(1.dp)

                            )
                            Text(
                                text = "Route info",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(
                                        horizontal = 8.dp
                                    )
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(0.3f))
                                    .height(1.dp)
                            )

                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        RouteInfoDataChips(data = data.route)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                val scheduleInOrder = data.schedule.sortedBy { it.schedule.sequence }
                if (scheduleInOrder.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .padding(
                                    vertical = 8.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(0.3f))
                                    .height(1.dp)
                            )
                            Text(
                                text = "Schedule resume",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(
                                        horizontal = 8.dp
                                    )
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(0.3f))
                                    .height(1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(
                        count = scheduleInOrder.size,
                        key = { position ->
                            scheduleInOrder[position].schedule.id
                        }
                    ) { position ->
                        ScheduleItem(
                            data = scheduleInOrder[position]
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun RouteInfoDataChips(
    data     : Route,
    modifier : Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        item {
            RightIconText(
                text = data.type.name,
                painter = painterResource(id = R.drawable.ic_route),
                contentDescription = null,
                textColor = MaterialTheme.colorScheme.surface,
                iconColor = MaterialTheme.colorScheme.surface,
                iconSize = 24.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(
                        vertical = 8.dp
                    )
                    .padding(
                        horizontal = 12.dp
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        item {
            RightIconText(
                text = data.totalDistance.toString() + " " + data.distanceUnit.name,
                painter = painterResource(id = R.drawable.ic_distance),
                contentDescription = null,
                textColor = MaterialTheme.colorScheme.surface,
                iconColor = MaterialTheme.colorScheme.surface,
                iconSize = 24.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(
                        vertical = 8.dp
                    )
                    .padding(
                        horizontal = 12.dp
                    )
            )
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
            RightIconText(
                text = data.totalDuration,
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = null,
                textColor = MaterialTheme.colorScheme.surface,
                iconColor = MaterialTheme.colorScheme.surface,
                iconSize = 24.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(
                        vertical = 8.dp
                    )
                    .padding(
                        horizontal = 12.dp
                    )
            )
        }
    }
}