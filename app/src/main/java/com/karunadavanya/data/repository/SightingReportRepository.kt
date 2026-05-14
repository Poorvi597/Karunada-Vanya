package com.karunadavanya.data.repository

import com.karunadavanya.data.local.AlertDao
import com.karunadavanya.data.local.AlertEntity
import com.karunadavanya.data.local.SightingReportDao
import com.karunadavanya.data.local.SightingReportEntity
import com.karunadavanya.data.remote.AlertRemoteDataSource
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.SightingReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class SightingReportRepository(
    private val sightingReportDao: SightingReportDao,
    private val alertDao: AlertDao,
    private val alertRemoteDataSource: AlertRemoteDataSource? = null
) {
    fun observeReports(): Flow<List<SightingReport>> {
        return sightingReportDao.observeAll().map { reports -> reports.map { it.asDomain() } }
    }

    suspend fun submitReport(
        species: String,
        description: String,
        locationDescription: String,
        time: String,
        date: String,
        timestamp: Long,
        latitude: Double?,
        longitude: Double?
    ) {
        val reportId = UUID.randomUUID().toString()
        val reportedAt = System.currentTimeMillis()
        sightingReportDao.insert(
            SightingReportEntity(
                id = reportId,
                species = species,
                locationDescription = locationDescription,
                latitude = latitude,
                longitude = longitude,
                time = time,
                date = date,
                createdAt = timestamp
            )
        )
        alertDao.insert(
            AlertEntity(
                id = "report_$reportId",
                title = "$species Sighting",
                type = AlertType.Sighting.name,
                location = locationDescription,
                description = description.ifBlank {
                    "$species was reported near $locationDescription. Stay alert and keep a safe distance."
                },
                reportedAt = reportedAt,
                priority = priorityFor(species).name
            )
        )
        alertRemoteDataSource?.let { remote ->
            runCatching {
                remote.postAlert(
                    animalType = species,
                    location = locationDescription,
                    description = description,
                    timestamp = reportedAt,
                    latitude = latitude,
                    longitude = longitude
                )
            }
        }
    }

    private fun priorityFor(value: String): AlertPriority {
        return when (value.lowercase()) {
            "tiger", "elephant", "leopard", "black panther", "sloth bear", "king cobra",
            "wild boar", "boar", "gaur", "gaur (indian bison)", "mugger crocodile" -> AlertPriority.High
            "hornbill", "great indian hornbill", "indian peafowl", "peacock",
            "common indian toad", "malabar gliding frog" -> AlertPriority.Low
            else -> AlertPriority.Medium
        }
    }
}
