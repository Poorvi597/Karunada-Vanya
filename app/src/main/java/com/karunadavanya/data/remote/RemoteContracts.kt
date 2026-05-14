package com.karunadavanya.data.remote

import com.karunadavanya.domain.CommunityAlert
import kotlinx.coroutines.flow.Flow

interface AlertRemoteDataSource {
    fun observeAlerts(): Flow<List<CommunityAlert>>
    suspend fun postAlert(
        animalType: String,
        location: String,
        description: String = "",
        timestamp: Long,
        latitude: Double? = null,
        longitude: Double? = null
    )
}

class FirebaseBackendNotConfiguredException(
    message: String = "Firebase backend is not configured. Add google-services.json and provide Firebase data sources."
) : IllegalStateException(message)
