package com.karunadavanya.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.karunadavanya.R
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.CommunityAlert
import com.karunadavanya.domain.Wildlife
import com.karunadavanya.domain.WildlifeCategory
import com.karunadavanya.domain.SightingReport

@Entity(tableName = "wildlife")
data class WildlifeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val scientificName: String,
    val category: String,
    val description: String,
    val habitat: String,
    val conservationStatus: String,
    val locations: String,
    val funFacts: String,
    @DrawableRes val imageResId: Int,
    @RawRes val soundResId: Int?,
    val soundUrl: String?
) {
    fun asDomain() = Wildlife(
        id = id,
        name = name,
        scientificName = scientificName,
        category = WildlifeCategory.valueOf(category),
        description = description,
        habitat = habitat,
        conservationStatus = conservationStatus,
        locations = locations.split("|").filter { it.isNotBlank() },
        funFacts = funFacts.split("|").filter { it.isNotBlank() },
        imageResId = imageResId,
        soundResId = soundResId ?: fallbackRawSoundResId(),
        soundUrl = soundUrl
    )

    @RawRes
    private fun fallbackRawSoundResId(): Int? {
        return when (id) {
            "asian_elephant" -> R.raw.elephant
            "bengal_tiger" -> R.raw.tiger
            "indian_leopard" -> R.raw.leopard
            "black_panther" -> R.raw.black_panther
            "mugger_crocodile" -> R.raw.crocodile
            "common_indian_toad" -> R.raw.indian_toad
            "malabar_gliding_frog" -> R.raw.gliding_frog
            "indian_peafowl" -> R.raw.peacock
            else -> null
        }
    }
}

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String,
    val location: String,
    val description: String,
    val reportedAt: Long,
    val priority: String
) {
    fun asDomain() = CommunityAlert(
        id = id,
        title = title,
        type = AlertType.valueOf(type),
        location = location,
        description = description,
        reportedAt = reportedAt,
        priority = AlertPriority.valueOf(priority)
    )
}

@Entity(tableName = "sighting_reports")
data class SightingReportEntity(
    @PrimaryKey val id: String,
    val species: String,
    val locationDescription: String,
    val latitude: Double?,
    val longitude: Double?,
    val time: String,
    val date: String,
    val createdAt: Long
) {
    fun asDomain() = SightingReport(
        id = id,
        species = species,
        locationDescription = locationDescription,
        latitude = latitude,
        longitude = longitude,
        time = time,
        date = date,
        createdAt = createdAt
    )
}
