package com.servinformacion.smart1sdkdemo.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.servinformacion.smart1sdk.android.core.model.CoordinatesData
import com.servinformacion.smart1sdk.android.order.types.OrderState
import com.servinformacion.smart1sdkdemo.core.components.ObserveAsEvents
import com.servinformacion.smart1sdkdemo.core.utils.IntentUtils
import com.servinformacion.smart1sdkdemo.core.utils.MapsUtils
import com.servinformacion.smart1sdkdemo.core.utils.PermissionUtils
import com.servinformacion.smart1sdkdemo.core.utils.ServiceUtils
import com.servinformacion.smart1sdkdemo.core.utils.ToastUtils
import com.servinformacion.smart1sdkdemo.home.components.LoadingDialog
import com.servinformacion.smart1sdkdemo.home.components.OrderDetailsDialog
import com.servinformacion.smart1sdkdemo.home.components.OrderPickerDialog
import com.servinformacion.smart1sdkdemo.home.components.PortDetailsDialog
import com.servinformacion.smart1sdkdemo.home.components.SelectableItem
import com.servinformacion.smart1sdkdemo.home.components.ToolbarHome
import com.servinformacion.smart1sdkdemo.tracker.TrackerDemo
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@ExperimentalPermissionsApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoot(
    viewModel : HomeViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()
    val orderDetailsBottomSheetState = rememberModalBottomSheetState()
    val selectOrderBottomSheetState = rememberModalBottomSheetState()
    val portDetailsBottomSheetState = rememberModalBottomSheetState()
    val backgroundLocationPermissions = rememberPermissionState(
        permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        onPermissionResult = {
            if (it) {
                viewModel.onAction(HomeAction.OnRefreshClicked)
            }
        }
    )

    val generalPermissions = rememberMultiplePermissionsState(
        permissions = PermissionUtils.getGeneralPermissionsToRequest(context),
        onPermissionsResult = {
            if (
                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) &&
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    backgroundLocationPermissions.launchPermissionRequest()
                } else {
                    viewModel.onAction(HomeAction.OnRefreshClicked)
                }
            }
        }
    )
    LaunchedEffect(Unit) {
        TrackerDemo.onGettingInOrOutOfPortFlow.collect { (port, type) ->
            viewModel.onAction(
                HomeAction.OnRefreshClicked
            )
        }
    }
    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        viewModel.onAction(
            HomeAction.OnStart(
                isTrackerRunning = ServiceUtils.isServiceRunning(
                    TrackerDemo::class.java,
                    context
                )
            )
        )
        if (
            PermissionUtils.getGeneralPermissionsToRequest(context).isNotEmpty()
        ) {
            generalPermissions.launchMultiplePermissionRequest()
        } else {
            if (
                !PermissionUtils.isAllLocationPermissionsGranted(context)
            ) {
                backgroundLocationPermissions.launchPermissionRequest()
            }
        }
    }
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            HomeEvent.GoToAppSettings -> {
                IntentUtils.goToAppDetailsSettings(context)
            }
            HomeEvent.StartTracker    -> {
                if (
                    PermissionUtils.isAllLocationPermissionsGranted(context)
                ) {
                    ServiceUtils.startForegroundService(
                        TrackerDemo::class.java,
                        context
                    )
                } else {
                    ToastUtils.showToast(
                        context,
                        "The location permissions are not granted. Please grant them in the app settings."
                    )
                }
            }
            HomeEvent.NotifyNewOrderInProgressToTracker -> {
                if (ServiceUtils.isServiceRunning(TrackerDemo::class.java, context)) {
                    TrackerDemo.notifyNewOrderInProgress()
                }
            }
            HomeEvent.StopTracker     -> {
                ServiceUtils.stopService(
                    TrackerDemo::class.java,
                    context
                )
            }
            is HomeEvent.OpenCloseOrderPicker -> {
                scope.launch {
                    if (event.shouldOpen) {
                        selectOrderBottomSheetState.show()
                    } else {
                        selectOrderBottomSheetState.hide()
                    }
                }
            }
            is HomeEvent.OpenCloseOrderDetails -> {
                scope.launch {
                    if (event.shouldOpen) {
                        orderDetailsBottomSheetState.show()
                    } else {
                        orderDetailsBottomSheetState.hide()
                    }
                }
            }
            is HomeEvent.UpdateMapCamera -> {
                scope.launch {
                    cameraPositionState.animate(
                        MapsUtils.fromCoordinatesDataToCameraUpdate(
                            coordinates = event.coordinates,
                            padding = 125,
                        ),
                    )
                }
            }
            is HomeEvent.OpenClosePortDetails -> {
                scope.launch {
                    if (event.shouldOpen) {
                        portDetailsBottomSheetState.show()
                    } else {
                        portDetailsBottomSheetState.hide()
                    }
                }
            }
            is HomeEvent.Error        -> {
                ToastUtils.showToast(
                    context,
                    event.msg
                )
            }
        }
    }
    val state by viewModel.state.collectAsState()
    HomeScreen(
        state                        = state,
        onAction                     = { viewModel.onAction(it) },
        selectOrderBottomSheetState  = selectOrderBottomSheetState ,
        orderDetailsBottomSheetState = orderDetailsBottomSheetState,
        portDetailsBottomSheetState  = portDetailsBottomSheetState ,
        cameraPositionState          = cameraPositionState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state                        : HomeState,
    onAction                     : (HomeAction) -> Unit,
    selectOrderBottomSheetState  : SheetState = rememberModalBottomSheetState(),
    orderDetailsBottomSheetState : SheetState = rememberModalBottomSheetState(),
    portDetailsBottomSheetState  : SheetState = rememberModalBottomSheetState(),
    cameraPositionState          : CameraPositionState = rememberCameraPositionState(),
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    ToolbarHome()
                },
                actions = {
                    Button(
                        onClick = {
                            onAction(
                                HomeAction.OnStartOrStopTracker(
                                    isTrackerRunning = ServiceUtils.isServiceRunning(
                                        TrackerDemo::class.java,
                                        context
                                    )
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!state.isTrackerEnabled) Color.Green else Color.Red,
                        )
                    ) {
                        Text(
                            text = if (!state.isTrackerEnabled) "Turn On Tracker" else "Turn Off Tracker",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                        )
                    }
                    IconButton(
                        onClick = {
                            onAction(
                                HomeAction.OnSettingsClicked
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(
                        onClick = {
                            onAction(
                                HomeAction.OnRefreshClicked
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Data"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (state.selectedOrder != null) {
                            "Order #${state.selectedOrder.order.id}"
                        } else {
                            "Select an order to visualize"
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    state.selectedOrder?.let { order ->
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(end = 8.dp)
                                .clickable {
                                    onAction(
                                        HomeAction.OnOrderInfoClicked(
                                            order
                                        )
                                    )
                                }
                        )
                    }
                }
                Box {
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties          = MapProperties(
                            isMyLocationEnabled = PermissionUtils.isAllLocationPermissionsGranted(context),
                            mapType             = MapType.NORMAL,
                            isTrafficEnabled    = false,
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                        ),
                    ) {
                        if (state.polylineCoordinates.isNotEmpty()) {
                            state.polylineCoordinates.forEach { somePolyline ->
                                Polyline(
                                    points = somePolyline.map {
                                        MapsUtils.fromCoordinatesDataToLatLng(it)
                                    },
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                        if (state.ports.isNotEmpty()) {
                            state.ports.forEachIndexed { index, somePort ->
                                val portCoordinates: CoordinatesData? = CoordinatesData(
                                    latitude = somePort.latitude,
                                    longitude = somePort.longitude,
                                )
                                if (portCoordinates != null) {
                                    val markerState = rememberMarkerState(
                                        position = MapsUtils.fromCoordinatesDataToLatLng(portCoordinates),
                                    )
                                    Marker(
                                        state   = markerState,
                                        title   = somePort.name,
                                        onClick = {
                                            onAction(HomeAction.OnSomePortClicked(somePort))
                                            false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    SelectableItem(
                        text = "Select a order",
                        textStyle = MaterialTheme.typography.bodyMedium,
                        textFontWeight = FontWeight.SemiBold,
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        onClick = { onAction(HomeAction.OnSelectOrderClicked) },
                        modifier = Modifier
                            .padding(
                                16.dp
                            )
                    )
                }
            }
            OrderPickerDialog(
                isOpen = state.isShowingOrderPicker,
                data = state.orders,
                onOrderSelected = { onAction(HomeAction.OnOrderSelected(it)) },
                onDismiss = { onAction(HomeAction.OnSelectOrderDismissed) },
                bottomSheetState = selectOrderBottomSheetState,
            )
            state.selectedPort?.let { selectedPort ->
                state.selectedOrder?.let { order ->
                    PortDetailsDialog(
                        isOpen = state.isShowingPortDetails,
                        data = selectedPort,
                        order = order,
                        onDismiss = { onAction(HomeAction.OnPortDetailsDismissed) },
                        bottomSheetState = portDetailsBottomSheetState,
                    )
                }
            }
            state.selectedOrder?.let { order ->
                OrderDetailsDialog(
                    isOpen = state.isShowingOrderDetails,
                    data = order,
                    onStateClick = {
                        if (it.order.state == OrderState.ASSIGNED) {
                            onAction(HomeAction.OnStartOrder(order = order))
                        }
                    },
                    onDismiss = { onAction(HomeAction.OnOrderDetailsDismissed) },
                    bottomSheetState = orderDetailsBottomSheetState,
                )
            }
            LoadingDialog(
                isOpen = state.isLoading,
                text = if (!state.currentLoadingMsg.isNullOrEmpty()) {
                    state.currentLoadingMsg
                } else { null },
                onDismiss = { },
            )
        }
    }
}