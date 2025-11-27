package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.servinformacion.smart1sdkdemo.R
import com.servinformacion.smart1sdkdemo.core.model.ScheduleContainer

@Composable
fun ScheduleItem(
    data     : ScheduleContainer,
    modifier : Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        CounterText(
            text = data.schedule.sequence.toString(),
            backgroundColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.surface,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    8.dp
                ),
            onClick = {
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(
                        bottom = 8.dp,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = data.schedule.job.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                MaterialTheme.colorScheme.tertiary
                            )
                            .padding(
                                8.dp
                            ),
                        text = data.schedule.stateName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.surface,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        RightIconText(
                            text = data.port.name,
                            painter = painterResource(id = R.drawable.ic_port),
                            iconSize = 16.dp,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                ),
                        )
                        RightIconText(
                            text = data.dock.name,
                            painter = painterResource(id = R.drawable.ic_dock),
                            iconSize = 16.dp,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                ),
                        )
                        RightIconText(
                            text = data.port.address,
                            imageVector = Icons.Default.Place,
                            iconSize = 16.dp,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                ),
                        )
                        RightIconText(
                            text = data.port.city,
                            painter = painterResource(id = R.drawable.ic_city),
                            iconSize = 16.dp,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                ),
                        )
                        RightIconText(
                            text = "${data.schedule.dateEventInit} ${data.schedule.hourEventInit} UTC", // You are welcome to convert this date to your local time
                            painter = painterResource(id = R.drawable.ic_init_date),
                            iconSize = 16.dp,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                ),
                        )
                        RightIconText(
                            text = "${data.schedule.dateEventEnd} ${data.schedule.hourEventEnd} UTC", // You are welcome to convert this date to your local time
                            painter = painterResource(id = R.drawable.ic_end_date),
                            iconSize = 16.dp,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}