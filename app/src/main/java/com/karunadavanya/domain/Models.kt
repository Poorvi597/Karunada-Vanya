package com.karunadavanya.domain

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Wildlife(
    val id: String,
    val name: String,
    val scientificName: String,
    val category: WildlifeCategory,
    val description: String,
    val habitat: String,
    val conservationStatus: String,
    val locations: List<String>,
    val funFacts: List<String>,
    @DrawableRes val imageResId: Int,
    @RawRes val soundResId: Int? = null,
    val soundUrl: String? = null
)

enum class WildlifeCategory(val label: String) {
    Mammals("Mammals"),
    Birds("Birds"),
    Reptiles("Reptiles"),
    Amphibians("Amphibians"),
    Trees("Trees")
}

data class CommunityAlert(
    val id: String,
    val title: String,
    val type: AlertType,
    val location: String,
    val description: String,
    val reportedAt: Long,
    val priority: AlertPriority,
    val animalType: String = title.removeSuffix(" Sighting"),
    val severity: String = priority.label,
    val reporter: String = "Community Reporter",
    val expiresAt: Long = reportedAt + 6 * 60 * 60 * 1000L,
    val latitude: Double? = null,
    val longitude: Double? = null
)

enum class AlertType(val label: String) {
    Sighting("Sighting"),
    Conflict("Conflict"),
    Emergency("Emergency")
}

enum class AlertPriority(val label: String) {
    Low("Low"),
    Medium("Medium"),
    High("High")
}

data class SightingReport(
    val id: String,
    val species: String,
    val locationDescription: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val time: String,
    val date: String,
    val createdAt: Long
)
