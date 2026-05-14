package com.karunadavanya.presentation

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadavanya.data.repository.AlertRepository
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.CommunityAlert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlertFeedUiState(
    val alerts: List<CommunityAlert> = emptyList()
)

data class AlertDetailUiState(
    val alert: CommunityAlert? = null
)

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val repository: AlertRepository
) : ViewModel() {
    private var userLocation: UserLocation? = null
    private val allAlerts = mutableListOf<CommunityAlert>()

    val alerts: SnapshotStateList<CommunityAlert> = mutableStateListOf()
    private val detailAlerts = MutableStateFlow<List<CommunityAlert>>(emptyList())

    val uiState: StateFlow<AlertFeedUiState> = detailAlerts
        .map { AlertFeedUiState(alerts = alerts.toList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AlertFeedUiState())

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
            repository.observeAlerts().collect { latestAlerts ->
                allAlerts.clear()
                allAlerts.addAll(latestAlerts)
                refreshVisibleAlerts()
            }
        }
    }

    fun detailState(alertId: String): StateFlow<AlertDetailUiState> {
        return detailAlerts
            .map { alerts -> AlertDetailUiState(alert = alerts.firstOrNull { it.id == alertId }) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AlertDetailUiState())
    }

    fun updateUserLocation(latitude: Double, longitude: Double) {
        userLocation = if (latitude.isValidLatitude() && longitude.isValidLongitude()) {
            UserLocation(latitude, longitude)
        } else {
            null
        }
        refreshVisibleAlerts()
    }

    private fun refreshVisibleAlerts() {
        val location = userLocation
        val now = System.currentTimeMillis()
        val visibleAlerts = allAlerts
            .filter { it.expiresAt > now }
            .filter { it.isWithinUserRadius(location) }
            .map { it.withNearbyConflictPriority(location) }
            .sortedByDescending { it.reportedAt }

        alerts.clear()
        alerts.addAll(visibleAlerts)
        detailAlerts.value = visibleAlerts
    }

    private fun CommunityAlert.withNearbyConflictPriority(location: UserLocation?): CommunityAlert {
        if (location == null || latitude == null || longitude == null || !animalType.isConflictAnimal()) {
            return this
        }

        return if (distanceMeters(location.latitude, location.longitude, latitude, longitude) < ALERT_RADIUS_METERS) {
            copy(priority = AlertPriority.High, severity = AlertPriority.High.label)
        } else {
            this
        }
    }

    private fun CommunityAlert.isWithinUserRadius(location: UserLocation?): Boolean {
        if (location == null || latitude == null || longitude == null) return true
        if (!latitude.isValidLatitude() || !longitude.isValidLongitude()) return true
        return distanceMeters(location.latitude, location.longitude, latitude, longitude) < ALERT_RADIUS_METERS
    }

    private fun Double.isValidLatitude(): Boolean = isFinite() && this in -90.0..90.0

    private fun Double.isValidLongitude(): Boolean = isFinite() && this in -180.0..180.0

    private fun String.isConflictAnimal(): Boolean {
        val normalized = lowercase()
        return conflictAnimals.any { it in normalized }
    }

    private fun distanceMeters(
        userLatitude: Double,
        userLongitude: Double,
        alertLatitude: Double,
        alertLongitude: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(userLatitude, userLongitude, alertLatitude, alertLongitude, results)
        return results[0]
    }

    private fun priorityFor(value: String): AlertPriority {
        return when (value.lowercase()) {
            "high", "tiger", "elephant", "leopard", "black panther", "sloth bear", "king cobra",
            "wild boar", "boar", "gaur", "bison", "monkey", "macaque" -> AlertPriority.High
            "low", "hornbill" -> AlertPriority.Low
            else -> AlertPriority.Medium
        }
    }

    private data class UserLocation(val latitude: Double, val longitude: Double)

    private companion object {
        const val ALERT_RADIUS_METERS = 10_000f
        const val ALERT_TTL_MILLIS = 6 * 60 * 60 * 1000L

        val conflictAnimals = setOf(
            "elephant",
            "wild boar",
            "boar",
            "leopard",
            "gaur",
            "bison",
            "tiger",
            "sloth bear",
            "monkey",
            "macaque"
        )
    }
}
