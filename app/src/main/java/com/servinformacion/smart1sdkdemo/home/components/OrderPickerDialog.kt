@file:OptIn(ExperimentalMaterial3Api::class)

package com.servinformacion.smart1sdkdemo.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer

@Composable
fun OrderPickerDialog(
    isOpen           : Boolean,
    data             : List<OrderContainer>,
    onOrderSelected  : (OrderContainer) -> Unit,
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    ),
            ) {
                Text(
                    text = "Select a order to visualize",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(16.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSurface.copy(0.3f))
                        .height(1.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 16.dp
                    )
                ) {
                    items(
                        count = data.size,
                        key = { position ->
                            data[position].order.id
                        }
                    ) { position ->
                        OrderItem(
                            data = data[position],
                            onClick = {
                                onOrderSelected(data[position])
                            }
                        )
                    }
                }
            }
        }
    }
}