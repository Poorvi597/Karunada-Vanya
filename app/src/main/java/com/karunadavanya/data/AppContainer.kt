package com.karunadavanya.data

import android.content.Context
import com.karunadavanya.data.local.KarunadaVanyaDatabase
import com.karunadavanya.data.remote.FirebaseRealtimeAlertDataSource
import com.karunadavanya.data.repository.AlertRepository
import com.karunadavanya.data.repository.SightingReportRepository
import com.karunadavanya.data.repository.WildlifeRepository

class AppContainer(context: Context) {
    private val database = KarunadaVanyaDatabase.getDatabase(context)
    private val firebaseAlerts = runCatching { FirebaseRealtimeAlertDataSource() }.getOrNull()

    val wildlifeRepository = WildlifeRepository(
        wildlifeDao = database.wildlifeDao()
    )

    val alertRepository = AlertRepository(
        alertDao = database.alertDao(),
        remoteDataSource = firebaseAlerts
    )

    val sightingReportRepository = SightingReportRepository(
        sightingReportDao = database.sightingReportDao(),
        alertDao = database.alertDao(),
        alertRemoteDataSource = firebaseAlerts
    )
}
