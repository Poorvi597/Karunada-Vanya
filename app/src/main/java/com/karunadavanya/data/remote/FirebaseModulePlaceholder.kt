package com.karunadavanya.data.remote

/*
 * This module is intentionally dependency-free so the app remains buildable
 * before Firebase project credentials are available.
 *
 * When Firebase is added:
 * - Implement AlertRemoteDataSource with Firestore snapshot listeners.
 * - Implement SightingReportRemoteDataSource with a Firestore collection write.
 * - Keep Room as the offline cache and sync boundary.
 */
object FirebaseModulePlaceholder {
    const val ALERTS_COLLECTION = "alerts"
    const val SIGHTING_REPORTS_COLLECTION = "sighting_reports"
}
