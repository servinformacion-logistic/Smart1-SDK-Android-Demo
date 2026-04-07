package com.servinformacion.smart1sdkdemo.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.servinformacion.smart1sdk.android.auth.LogoutAndFinishSession
import com.servinformacion.smart1sdk.android.core.ResultS1SDK
import com.servinformacion.smart1sdk.android.core.model.CoordinatesData
import com.servinformacion.smart1sdk.android.dock.GetAndSearchDocksByPage
import com.servinformacion.smart1sdk.android.dock.model.Dock
import com.servinformacion.smart1sdk.android.dock.model.DockFilters
import com.servinformacion.smart1sdk.android.dock.types.DockProperty
import com.servinformacion.smart1sdk.android.order.GetAndSearchOrdersByPage
import com.servinformacion.smart1sdk.android.order.UpdateOrderState
import com.servinformacion.smart1sdk.android.order.types.OrderProperty
import com.servinformacion.smart1sdk.android.order.types.OrderState
import com.servinformacion.smart1sdk.android.port.GetAndSearchPortsByPage
import com.servinformacion.smart1sdk.android.port.model.Port
import com.servinformacion.smart1sdk.android.port.model.PortFilters
import com.servinformacion.smart1sdk.android.port.types.PortProperty
import com.servinformacion.smart1sdk.android.route.GetAndSearchRoutesByPage
import com.servinformacion.smart1sdk.android.route.model.Route
import com.servinformacion.smart1sdk.android.route.model.RouteFilters
import com.servinformacion.smart1sdk.android.route.types.RouteProperty
import com.servinformacion.smart1sdk.android.schedule.GetAndSearchScheduleByPage
import com.servinformacion.smart1sdk.android.schedule.model.ScheduleFilters
import com.servinformacion.smart1sdk.android.schedule.types.ScheduleProperty
import com.servinformacion.smart1sdkdemo.core.model.OrderContainer
import com.servinformacion.smart1sdkdemo.core.model.ScheduleContainer
import com.servinformacion.smart1sdkdemo.core.utils.CoordinateUtils
import com.servinformacion.smart1sdkdemo.core.utils.ErrorUtils
import com.servinformacion.smart1sdkdemo.core.utils.PaginationUtils
import com.servinformacion.smart1sdkdemo.core.utils.StartSDKUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(

): ViewModel() {

    private  val _state = MutableStateFlow(HomeState())
    var state = _state.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )
    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    private val getOrdersFromSDK   : GetAndSearchOrdersByPage   = GetAndSearchOrdersByPage()
    private val getScheduleFromSDK : GetAndSearchScheduleByPage = GetAndSearchScheduleByPage()
    private val getPortsFromSDK    : GetAndSearchPortsByPage    = GetAndSearchPortsByPage()
    private val getDocksFromSDK    : GetAndSearchDocksByPage    = GetAndSearchDocksByPage()
    private val getRoutesFromSDK   : GetAndSearchRoutesByPage   = GetAndSearchRoutesByPage()
    private val updateOrderState   : UpdateOrderState           = UpdateOrderState()

    fun onAction(
        action : HomeAction
    ) {
        when (action) {
            is HomeAction.OnStart -> {
                _state.update {
                    it.copy(
                        isTrackerEnabled = action.isTrackerRunning,
                    )
                }
            }
            is HomeAction.OnStartOrStopTracker -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isTrackerEnabled = !action.isTrackerRunning,
                        )
                    }
                    if (action.isTrackerRunning) {
                        LogoutAndFinishSession().invoke()
                        eventChannel.send(HomeEvent.StopTracker)
                    } else {
                        StartSDKUtils.startSDK()
                        eventChannel.send(HomeEvent.StartTracker)
                    }
                }
            }
            is HomeAction.OnRefreshClicked -> handleOnRefreshData(forceRefresh = true)
            HomeAction.OnSettingsClicked -> {
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.GoToAppSettings)
                }
            }
            HomeAction.OnSelectOrderClicked -> handleSelectOrderClicked()
            is HomeAction.OnOrderSelected -> handleOrderSelected(action.order)
            is HomeAction.OnOrderInfoClicked -> handleSeeOrderDetails(action.order)
            is HomeAction.OnStartOrder -> handleStartOrder(action.order)
            is HomeAction.OnSomePortClicked -> handleOnPortClicked(action.port)
            HomeAction.OnSelectOrderDismissed -> {
                _state.update {
                    it.copy(
                        isShowingOrderPicker = false,
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.OpenCloseOrderPicker(false))
                }
            }
            HomeAction.OnOrderDetailsDismissed -> {
                _state.update {
                    it.copy(
                        isShowingOrderDetails = false,
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.OpenCloseOrderDetails(false))
                }
            }
            HomeAction.OnPortDetailsDismissed -> {
                _state.update {
                    it.copy(
                        isShowingPortDetails = false,
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(HomeEvent.OpenClosePortDetails(false))
                }
            }
        }
    }

    private fun handleOnRefreshData(
        forceRefresh : Boolean = false
    ) {
        viewModelScope.launch {
            if (
                if (
                    !forceRefresh
                ) {
                    _state.value.orders.isNotEmpty()
                } else {
                    false
                }
            ) {
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading = true,
                    currentLoadingMsg = "Loading Orders..."
                )
            }
            val orders = PaginationUtils.getPaginatedData(
                getData = {
                    val res = getOrdersFromSDK.invoke(
                        fields = listOf(
                            OrderProperty.ID,
                            OrderProperty.ROUTE_ID,
                            OrderProperty.STATE,
                        )
                    )
                    when (res) {
                        is ResultS1SDK.Success -> return@getPaginatedData res.data
                        is ResultS1SDK.Error -> {
                            Timber.e(Exception(ErrorUtils.toMsg(res.error)))
                        }
                    }
                    return@getPaginatedData null
                },
                onProgress = { currentDownloadedRecords, totalRecords ->
                    _state.update {
                        it.copy(
                            isLoading = true,
                            currentLoadingMsg = "Loading Orders\n [$currentDownloadedRecords from $totalRecords]"
                        )
                    }
                }
            )
            if (orders.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentLoadingMsg = null
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(
                        HomeEvent.Error(
                            msg = "No orders found"
                        )
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading = true,
                    currentLoadingMsg = "Loading Schedule..."
                )
            }
            val schedule = PaginationUtils.getPaginatedData(
                getData = {
                    val res = getScheduleFromSDK.invoke(
                        fields = listOf(
                            ScheduleProperty.ID,
                            ScheduleProperty.ORDER_ID,
                            ScheduleProperty.DOCK_ID,
                            ScheduleProperty.SEQUENCE,
                            ScheduleProperty.STATE_NAME,
                            ScheduleProperty.JOB,
                            ScheduleProperty.DATE_EVENT_INIT,
                            ScheduleProperty.HOUR_EVENT_INIT,
                            ScheduleProperty.DATE_EVENT_END,
                            ScheduleProperty.HOUR_EVENT_END,
                        ),
                        filters = ScheduleFilters(
                            orderIds = orders.distinctBy { it.id }.map { it.id }
                        )
                    )
                    when (res) {
                        is ResultS1SDK.Success -> return@getPaginatedData res.data
                        is ResultS1SDK.Error -> {
                            Timber.e(Exception(ErrorUtils.toMsg(res.error)))
                        }
                    }
                    return@getPaginatedData null
                },
                onProgress = { currentDownloadedRecords, totalRecords ->
                    _state.update {
                        it.copy(
                            isLoading = true,
                            currentLoadingMsg = "Loading Schedule\n [$currentDownloadedRecords from $totalRecords]"
                        )
                    }
                }
            )
            if (schedule.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentLoadingMsg = null
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(
                        HomeEvent.Error(
                            msg = "No schedule found for related orders ${orders.distinctBy { it.id }.map { it.id }}"
                        )
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading = true,
                    currentLoadingMsg = "Loading Docks..."
                )
            }
            val docks = PaginationUtils.getPaginatedData(
                getData = {
                    val res = getDocksFromSDK.invoke(
                        fields = listOf(
                            DockProperty.ID,
                            DockProperty.NAME,
                            DockProperty.PICKUP_TIME,
                            DockProperty.DELIVERY_TIME,
                            DockProperty.PORT_ID,
                            DockProperty.STATUS,
                            DockProperty.FLEET,
                        ),
                        filters = DockFilters(
                            ids = schedule.distinctBy { it.dockId }.map { it.dockId }
                        )
                    )
                    when (res) {
                        is ResultS1SDK.Success -> return@getPaginatedData res.data
                        is ResultS1SDK.Error -> {
                            Timber.e(Exception(ErrorUtils.toMsg(res.error)))
                        }
                    }
                    return@getPaginatedData null
                },
                onProgress = { currentDownloadedRecords, totalRecords ->
                    _state.update {
                        it.copy(
                            isLoading = true,
                            currentLoadingMsg = "Loading Docks\n [$currentDownloadedRecords from $totalRecords]"
                        )
                    }
                }
            )
            if (docks.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading         = false,
                        currentLoadingMsg = null,
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(
                        HomeEvent.Error(
                            msg = "No docks found for related schedule ${schedule.distinctBy { it.dockId }.map { it.dockId }}"
                        )
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading         = true,
                    currentLoadingMsg = "Loading Ports...",
                )
            }
            val ports = PaginationUtils.getPaginatedData(
                getData = {
                    val res = getPortsFromSDK.invoke(
                        fields = listOf(
                            PortProperty.ID,
                            PortProperty.NAME,
                            PortProperty.DESCRIPTION,
                            PortProperty.IDENTIFICATION,
                            PortProperty.COUNTRY,
                            PortProperty.DEPARTMENT,
                            PortProperty.CITY,
                            PortProperty.ADDRESS,
                            PortProperty.LATITUDE,
                            PortProperty.LONGITUDE,
                            PortProperty.STATUS,
                        ),
                        filters = PortFilters(
                            ids = docks.distinctBy { it.portId }.map { it.portId }
                        )
                    )
                    when (res) {
                        is ResultS1SDK.Success -> return@getPaginatedData res.data
                        is ResultS1SDK.Error -> {
                            Timber.e(Exception(ErrorUtils.toMsg(res.error)))
                        }
                    }
                    return@getPaginatedData null
                },
                onProgress = { currentDownloadedRecords, totalRecords ->
                    _state.update {
                        it.copy(
                            isLoading         = true,
                            currentLoadingMsg = "Loading Ports\n [$currentDownloadedRecords from $totalRecords]",
                        )
                    }
                }
            )
            if (ports.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        currentLoadingMsg = null
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(
                        HomeEvent.Error(
                            msg = "No ports found for related docks ${docks.distinctBy { it.portId }.map { it.portId }}"
                        )
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading         = true,
                    currentLoadingMsg = "Loading Routes...",
                )
            }
            val routes = PaginationUtils.getPaginatedData(
                getData = {
                    val res = getRoutesFromSDK.invoke(
                        fields = listOf(
                            RouteProperty.ID,
                            RouteProperty.NAME,
                            RouteProperty.DESCRIPTION,
                            RouteProperty.TYPE,
                            RouteProperty.DISTANCE_UNIT,
                            RouteProperty.TOTAL_DURATION,
                            RouteProperty.TOTAL_DISTANCE,
                            RouteProperty.ROUTE_INFO,
                            RouteProperty.STATUS,
                        ),
                        filters = RouteFilters(
                            ids = orders.distinctBy { it.routeId }.map { it.routeId }
                        )
                    )
                    when (res) {
                        is ResultS1SDK.Success -> return@getPaginatedData res.data
                        is ResultS1SDK.Error -> {
                            Timber.e(Exception(ErrorUtils.toMsg(res.error)))
                        }
                    }
                    return@getPaginatedData null
                },
                onProgress = { currentDownloadedRecords, totalRecords ->
                    _state.update {
                        it.copy(
                            isLoading         = true,
                            currentLoadingMsg = "Loading Routes\n [$currentDownloadedRecords from $totalRecords]",
                        )
                    }
                }
            )
            if (routes.isEmpty()) {
                _state.update {
                    it.copy(
                        isLoading         = false,
                        currentLoadingMsg = null,
                    )
                }
                viewModelScope.launch {
                    eventChannel.send(
                        HomeEvent.Error(
                            msg = "No routes found for related orders ${orders.distinctBy { it.routeId }.map { it.routeId }}"
                        )
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading         = true,
                    currentLoadingMsg = "Mapping Data...",
                )
            }
            val ordersWithRelatedData : MutableList<OrderContainer> = mutableListOf()
            orders.forEach { order ->
                val relatedSchedule : MutableList<ScheduleContainer> = mutableListOf()
                schedule.filter { it.orderId == order.id }.distinctBy { it.id }.forEach { schedule ->
                    val relatedDock : Dock? = docks.firstOrNull { it.id == schedule.dockId }
                    if (relatedDock == null) return@forEach
                    val relatedPort : Port? = ports.firstOrNull { it.id == relatedDock.portId }
                    if (relatedPort == null) return@forEach
                    relatedSchedule.add(
                        ScheduleContainer(
                            schedule = schedule,
                            port     = relatedPort,
                            dock     = relatedDock,
                        )
                    )
                }
                val relatedRoute: Route? = routes.firstOrNull { it.id == order.routeId }
                if (relatedRoute == null) return@forEach
                ordersWithRelatedData.add(
                    OrderContainer(
                        order    = order,
                        schedule = relatedSchedule,
                        route    = relatedRoute,
                    )
                )
            }
            if (_state.value.selectedOrder != null) {
                _state.update {
                    it.copy(
                        selectedOrder       = null,
                        polylineCoordinates = emptyList(),
                        ports               = emptyList(),
                    )
                }
                delay(100)
            }
            _state.update {
                it.copy(
                    isLoading         = false,
                    currentLoadingMsg = null,
                    orders            = ordersWithRelatedData,
                )
            }
        }
    }

    private fun handleStartOrder(
        order : OrderContainer
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    currentLoadingMsg = "Starting order..."
                )
            }
            val result = updateOrderState
                .invoke(
                    orderId   = order.order.id,
                    newStatus = OrderState.IN_PROGRESS,
                )
            when (result) {
                is ResultS1SDK.Success -> {
                    _state.update {
                        it.copy(
                            isLoading         = false,
                            currentLoadingMsg = null,
                            orders            = it.orders.map { orderInt ->
                                if (orderInt.order.id == order.order.id) {
                                    orderInt.copy(
                                        order = orderInt.order.copy(
                                            state = OrderState.IN_PROGRESS
                                        )
                                    )
                                } else {
                                    orderInt
                                }
                          },
                            selectedOrder = it.selectedOrder?.copy(
                                order = it.selectedOrder.order.copy(
                                    state = OrderState.IN_PROGRESS
                                )
                            )
                        )
                    }
                }
                is ResultS1SDK.Error -> {
                    _state.update {
                        it.copy(
                            isLoading         = false,
                            currentLoadingMsg = null,
                        )
                    }
                    viewModelScope.launch {
                        eventChannel.send(
                            HomeEvent.Error(
                                msg = ErrorUtils.toMsg(result.error)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleSelectOrderClicked() {
        if (
            _state.value.orders.isEmpty()
        ) {
            viewModelScope.launch {
                eventChannel.send(HomeEvent.Error("No orders found on current session"))
            }
            return
        }
        _state.update {
            it.copy(
                isShowingOrderPicker = true,
            )
        }
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OpenCloseOrderPicker(true))
        }
    }

    private fun handleOrderSelected(
        order : OrderContainer
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isShowingOrderPicker  = false,
                )
            }
            eventChannel.send(HomeEvent.OpenCloseOrderPicker(false))
            if (_state.value.selectedOrder != null) {
                _state.update {
                    it.copy(
                        selectedOrder       = null,
                        polylineCoordinates = emptyList(),
                        ports               = emptyList(),
                    )
                }
                delay(100)
            }
            _state.update {
                it.copy(
                    isLoading     = false,
                    selectedOrder = order,
                )
            }
            val portsToVisit: List<Port> = order.schedule
                .sortedBy { it.schedule.sequence }
                .map { it.port }
                .distinctBy { it.id }
            val routePolyline: List<String> = order.route.routeInfo
                .sortedByDescending { it.sequence }
                .map { it.segmentPolyline }
            if (routePolyline.isNotEmpty()) {
                val polylineCoordinates: MutableList<List<CoordinatesData>> = mutableListOf()
                routePolyline.forEach { polyline ->
                    polylineCoordinates.add(CoordinateUtils.decodePolyline(polyline))
                }
                eventChannel.send(
                    HomeEvent.UpdateMapCamera(
                        polylineCoordinates.flatten()
                    )
                )
                _state.update {
                    it.copy(
                        polylineCoordinates = polylineCoordinates,
                        ports               = portsToVisit,
                    )
                }
            }
        }
    }

    private fun handleSeeOrderDetails(
        order : OrderContainer
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading             = false,
                    isShowingOrderDetails = true,
                    selectedOrder         = order,
                )
            }
            eventChannel.send(HomeEvent.OpenCloseOrderDetails(true))
        }
    }

    private fun handleOnPortClicked(
        port : Port
    ) {
        _state.update {
            it.copy(
                isShowingPortDetails = true,
                selectedPort         = port,
            )
        }
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OpenClosePortDetails(true))
        }
    }
}