@file:OptIn(ExperimentalMaterial3Api::class)

package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.servinformacion.smart1sdk.android.port.model.Port
import com.servinformacion.smart1sdkdemo.R
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer
import com.servinformacion.smart1sdkdemo.core.model.ScheduleContainer

@Composable
fun PortDetailsDialog(
    isOpen           : Boolean,
    data             : Port,
    order            : OrderContainer,
    onDismiss        : () -> Unit,
    bottomSheetState : SheetState = rememberModalBottomSheetState(),
    modifier         : Modifier = Modifier
) {
    val schedulePortFiltered : List<ScheduleContainer> = order.schedule.filter { it.port.id == data.id }
    if (
        isOpen &&
        schedulePortFiltered.isNotEmpty()
    ) {
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
                        text = data.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (data.description.isNotEmpty()) {
                    item {
                        Text(
                            text = data.description,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                item {
                    PortInfoDataChips(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = data,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                val scheduleInOrder = schedulePortFiltered.sortedBy { it.schedule.sequence }
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
                            data = scheduleInOrder[position],
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PortInfoDataChips(
    data     : Port,
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
                text = data.city,
                painter = painterResource(id = R.drawable.ic_city),
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
                text = data.address,
                imageVector = Icons.Default.Place,
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
                text = data.identification,
                painter = painterResource(id = R.drawable.ic_card),
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