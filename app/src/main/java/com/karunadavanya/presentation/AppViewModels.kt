package com.karunadavanya.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.karunadavanya.data.repository.AlertRepository
import com.karunadavanya.data.repository.SightingReportRepository
import com.karunadavanya.data.repository.WildlifeRepository
import com.karunadavanya.domain.CommunityAlert
import com.karunadavanya.domain.SightingReport
import com.karunadavanya.domain.Wildlife
import com.karunadavanya.domain.WildlifeCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class WildlifeViewModel(
    private val repository: WildlifeRepository
) : ViewModel() {
    val query = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<WildlifeCategory?>(null)

    val wildlife: StateFlow<List<Wildlife>> = combine(query, selectedCategory) { text, category ->
        text to category
    }.flatMapLatest { (text, category) ->
        repository.observeWildlife(text, category)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { repository.seedIfNeeded() }
    }

    fun updateQuery(value: String) {
        query.value = value
    }

    fun selectCategory(category: WildlifeCategory?) {
        selectedCategory.value = category
    }
}

class WildlifeDetailViewModel(
    repository: WildlifeRepository,
    wildlifeId: String
) : ViewModel() {
    val wildlife: StateFlow<Wildlife?> = repository.observeWildlife(wildlifeId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

class AlertsViewModel(
    private val repository: AlertRepository
) : ViewModel() {
    val alerts: SnapshotStateList<CommunityAlert> = mutableStateListOf()

    init {
        viewModelScope.launch {
            repository.seedIfNeeded()
            repository.observeAlerts().collect { latestAlerts ->
                val cutoff = System.currentTimeMillis() - SIX_HOURS_MILLIS
                alerts.clear()
                alerts.addAll(latestAlerts.filter { it.reportedAt >= cutoff })
            }
        }
    }
}

private const val SIX_HOURS_MILLIS = 6 * 60 * 60 * 1000L

class AlertDetailViewModel(
    repository: AlertRepository,
    alertId: String
) : ViewModel() {
    val alert: StateFlow<CommunityAlert?> = repository.observeAlert(alertId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

data class ReportFormState(
    val species: String = "",
    val description: String = "",
    val locationDescription: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val time: String = "",
    val date: String = "",
    val timestamp: Long? = null,
    val submittedMessage: String? = null
) {
    val canSubmit: Boolean
        get() = species.isNotBlank() &&
            description.isNotBlank() &&
            locationDescription.isNotBlank() &&
            time.isNotBlank() &&
            date.isNotBlank() &&
            timestamp != null
}

class ReportViewModel(
    private val repository: SightingReportRepository
) : ViewModel() {
    val formState = MutableStateFlow(ReportFormState())

    val reports: StateFlow<List<SightingReport>> = repository.observeReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun updateSpecies(value: String) {
        formState.value = formState.value.copy(species = value, submittedMessage = null)
    }

    fun updateDescription(value: String) {
        formState.value = formState.value.copy(description = value, submittedMessage = null)
    }

    fun updateLocation(value: String) {
        formState.value = formState.value.copy(locationDescription = value, submittedMessage = null)
    }

    fun updateCoordinates(latitude: Double?, longitude: Double?) {
        formState.value = formState.value.copy(
            latitude = latitude,
            longitude = longitude,
            submittedMessage = null
        )
    }

    fun updateTime(value: String) {
        formState.value = formState.value.copy(time = value, submittedMessage = null)
    }

    fun updateDate(value: String) {
        formState.value = formState.value.copy(date = value, submittedMessage = null)
    }

    fun updateTimestamp(date: String, time: String, timestamp: Long) {
        formState.value = formState.value.copy(
            date = date,
            time = time,
            timestamp = timestamp,
            submittedMessage = null
        )
    }

    fun submit() {
        val current = formState.value
        if (!current.canSubmit) {
            formState.value = current.copy(submittedMessage = "Complete all report fields before submitting.")
            return
        }

        viewModelScope.launch {
            runCatching {
                repository.submitReport(
                    species = current.species,
                    description = current.description,
                    locationDescription = current.locationDescription,
                    time = current.time,
                    date = current.date,
                    timestamp = checkNotNull(current.timestamp),
                    latitude = current.latitude,
                    longitude = current.longitude
                )
            }.onSuccess {
                formState.value = ReportFormState(
                    submittedMessage = "Sighting alert posted to Firebase."
                )
            }.onFailure { error ->
                formState.value = current.copy(
                    submittedMessage = error.message ?: "Could not post alert. Check Firebase configuration."
                )
            }
        }
    }
}

class SimpleViewModelFactory<T : ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator.invoke() as T
}
