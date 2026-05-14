package com.karunadavanya.data.repository

import com.karunadavanya.data.SampleData
import com.karunadavanya.data.local.AlertDao
import com.karunadavanya.data.local.AlertEntity
import com.karunadavanya.data.remote.AlertRemoteDataSource
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.CommunityAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID

class AlertRepository(
    private val alertDao: AlertDao,
    private val remoteDataSource: AlertRemoteDataSource? = null
) {
    suspend fun seedIfNeeded() {
        if (alertDao.count() == 0) {
            alertDao.insertAll(SampleData.alerts)
        }
    }

    fun observeAlerts(): Flow<List<CommunityAlert>> {
        val localAlerts = alertDao.observeAll().map { rows -> rows.map { it.asDomain() } }
        val remoteAlerts = remoteDataSource?.observeAlerts()
            ?.catch { emit(emptyList()) }
            ?: return localAlerts

        return combine(remoteAlerts, localAlerts) { remote, local ->
            (remote + local)
                .distinctBy { alert ->
                    listOf(alert.animalType, alert.location, alert.reportedAt, alert.description).joinToString("|")
                }
                .sortedByDescending { it.reportedAt }
        }
    }

    fun observeAlert(id: String): Flow<CommunityAlert?> {
        return alertDao.observeById(id).map { it?.asDomain() }
    }

    suspend fun postAlert(
        animalType: String,
        location: String,
        timestamp: Long,
        description: String = "$animalType was reported near $location. Stay alert and keep a safe distance.",
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        val priority = priorityFor(animalType)
        val reportedAt = System.currentTimeMillis()
        alertDao.insert(
            AlertEntity(
                id = "local_${UUID.randomUUID()}",
                title = "$animalType Sighting",
                type = AlertType.Sighting.name,
                location = location,
                description = description,
                reportedAt = reportedAt,
                priority = priority.name
            )
        )
        remoteDataSource?.let { remote ->
            runCatching {
                remote.postAlert(
                    animalType = animalType,
                    location = location,
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
            "wild boar", "boar", "gaur", "bison", "mugger crocodile" -> AlertPriority.High
            "hornbill", "great indian hornbill", "indian peafowl", "peacock",
            "common indian toad", "malabar gliding frog" -> AlertPriority.Low
            else -> AlertPriority.Medium
        }
    }
}
