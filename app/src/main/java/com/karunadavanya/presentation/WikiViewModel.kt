package com.karunadavanya.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karunadavanya.data.repository.WildlifeRepository
import com.karunadavanya.domain.Wildlife
import com.karunadavanya.domain.WildlifeCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

data class WildlifeUiState(
    val species: List<WildlifeListItem> = emptyList(),
    val isLoading: Boolean = false
)

data class WildlifeListItem(
    val id: Int,
    val rawId: String,
    val name: String,
    @DrawableRes val imageResId: Int,
    @RawRes val soundResId: Int?,
    val description: String,
    val category: String,
    val scientificName: String
)

data class WildlifeDetailUiState(
    val species: WildlifeDetailItem? = null,
    val geminiFact: String = "This species is a vital part of the Western Ghats ecosystem, helping maintain the delicate balance of Karnataka's biodiversity."
)

data class WildlifeDetailItem(
    val id: Int,
    val name: String,
    @DrawableRes val imageResId: Int,
    val description: String,
    val habitat: String,
    val conservationStatus: String,
    val funFact: String,
    val rawId: String,
    @RawRes val soundResId: Int?,
    val scientificName: String,
    val locations: List<String>
)

@HiltViewModel
class WikiViewModel @Inject constructor(
    private val repository: WildlifeRepository
) : ViewModel() {
    
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<WildlifeCategory?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WildlifeUiState> = combine(searchQuery, selectedCategory) { query, category ->
        query to category
    }.flatMapLatest { (query, category) ->
        repository.observeWildlife(query, category)
    }.map { wildlife ->
        WildlifeUiState(
            species = wildlife.map { it.toListItem() },
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WildlifeUiState(isLoading = true))

    init {
        viewModelScope.launch { repository.seedIfNeeded() }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun selectCategory(category: WildlifeCategory?) {
        selectedCategory.value = if (selectedCategory.value == category) null else category
    }

    fun detailState(wildlifeId: Int): StateFlow<WildlifeDetailUiState> {
        return repository.observeWildlife("", null)
            .map { wildlife ->
                val item = wildlife.firstOrNull { it.stableIntId() == wildlifeId }
                WildlifeDetailUiState(species = item?.toDetailItem())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WildlifeDetailUiState())
    }
}

private fun Wildlife.stableIntId(): Int = id.hashCode().absoluteValue

private fun Wildlife.toListItem(): WildlifeListItem = WildlifeListItem(
    id = stableIntId(),
    rawId = id,
    name = name,
    imageResId = imageResId,
    soundResId = soundResId,
    description = description,
    category = category.label,
    scientificName = scientificName
)

private fun Wildlife.toDetailItem(): WildlifeDetailItem {
    return WildlifeDetailItem(
        id = stableIntId(),
        name = name,
        imageResId = imageResId,
        description = description,
        habitat = habitat,
        conservationStatus = conservationStatus,
        funFact = funFacts.firstOrNull().orEmpty(),
        rawId = id,
        soundResId = soundResId,
        scientificName = scientificName,
        locations = locations
    )
}
