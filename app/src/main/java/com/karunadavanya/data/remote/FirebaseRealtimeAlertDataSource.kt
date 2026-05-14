package com.karunadavanya.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.karunadavanya.domain.AlertPriority
import com.karunadavanya.domain.AlertType
import com.karunadavanya.domain.CommunityAlert
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class FirebaseAlertDto(
    val animalType: String = "",
    val location: String = "",
    val timestamp: Long = 0L,
    val severity: String = "",
    val reporter: String = "",
    val expiresAt: Long = 0L,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val title: String = "",
    val description: String = ""
)

class FirebaseRealtimeAlertDataSource(
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AlertRemoteDataSource {
    private val alertsRef = firestore.collection(FirebaseModulePlaceholder.ALERTS_COLLECTION)

    override fun observeAlerts(): Flow<List<CommunityAlert>> = callbackFlow {
        val registration = alertsRef
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val alerts = snapshot?.documents.orEmpty().mapNotNull { document ->
                    val dto = document.toObject(FirebaseAlertDto::class.java) ?: return@mapNotNull null
                    val animalType = dto.animalType.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                    val timestamp = dto.timestamp.takeIf { it > 0L } ?: return@mapNotNull null
                    val priority = priorityFor(dto.severity.ifBlank { animalType })
                    val location = dto.location.takeIf { it.isNotBlank() }
                        ?: nearestKnownKarnatakaLandmark(dto.latitude, dto.longitude)

                    CommunityAlert(
                        id = document.id,
                        title = dto.title.ifBlank { "$animalType Sighting" },
                        type = AlertType.Sighting,
                        location = location,
                        description = dto.description.ifBlank {
                            "$animalType was reported near $location. Stay alert and keep a safe distance."
                        },
                        reportedAt = timestamp,
                        priority = priority,
                        animalType = animalType,
                        severity = dto.severity.ifBlank { priority.label },
                        reporter = dto.reporter.ifBlank { "Community Reporter" },
                        expiresAt = dto.expiresAt.takeIf { it > 0L } ?: (timestamp + ALERT_TTL_MILLIS),
                        latitude = dto.latitude,
                        longitude = dto.longitude
                    )
                }

                trySend(alerts)
            }

        awaitClose { registration.remove() }
    }

    override suspend fun postAlert(
        animalType: String,
        location: String,
        description: String,
        timestamp: Long,
        latitude: Double?,
        longitude: Double?
    ) {
        val priority = priorityFor(animalType)
        alertsRef.add(
            FirebaseAlertDto(
                animalType = animalType,
                location = location,
                timestamp = timestamp,
                severity = priority.label,
                reporter = "Karunada Vanya User",
                expiresAt = timestamp + ALERT_TTL_MILLIS,
                latitude = latitude,
                longitude = longitude,
                title = "$animalType Sighting",
                description = description.ifBlank {
                    "$animalType was reported near $location. Stay alert and keep a safe distance."
                }
            )
        ).await()
    }

    private fun priorityFor(value: String): AlertPriority {
        return when (value.lowercase()) {
            "high", "tiger", "elephant", "leopard", "black panther", "sloth bear", "king cobra" -> AlertPriority.High
            "low", "hornbill" -> AlertPriority.Low
            else -> AlertPriority.Medium
        }
    }

    private fun nearestKnownKarnatakaLandmark(latitude: Double?, longitude: Double?): String {
        if (latitude == null || longitude == null) return "Near Karnataka Forest Range"
        return karnatakaLandmarks.minBy { landmark ->
            val latitudeDelta = latitude - landmark.latitude
            val longitudeDelta = longitude - landmark.longitude
            latitudeDelta * latitudeDelta + longitudeDelta * longitudeDelta
        }.label
    }

    private data class KarnatakaLandmark(
        val label: String,
        val latitude: Double,
        val longitude: Double
    )

    private companion object {
        const val ALERT_TTL_MILLIS = 6 * 60 * 60 * 1000L

        val karnatakaLandmarks = listOf(
            KarnatakaLandmark("Kabini, Nagarhole", 11.9041, 76.2692),
            KarnatakaLandmark("Bandipur Tiger Reserve", 11.6670, 76.6300),
            KarnatakaLandmark("Daroji Sloth Bear Sanctuary", 15.2449, 76.5891),
            KarnatakaLandmark("Agumbe Rainforest", 13.5087, 75.0958),
            KarnatakaLandmark("Bhadra Tiger Reserve", 13.6137, 75.6320),
            KarnatakaLandmark("Ranganathittu Bird Sanctuary", 12.4216, 76.6565),
            KarnatakaLandmark("Bannerghatta National Park", 12.8004, 77.5770),
            KarnatakaLandmark("Kudremukh National Park", 13.2142, 75.2507),
            KarnatakaLandmark("Bengaluru Urban Forest Division", 12.9716, 77.5946),
            KarnatakaLandmark("Cauvery Wildlife Sanctuary", 12.1190, 77.3441)
        )
    }
}
