package com.karunadavanya.di

import android.content.Context
import com.karunadavanya.data.local.AlertDao
import com.karunadavanya.data.local.KarunadaVanyaDatabase
import com.karunadavanya.data.local.SightingReportDao
import com.karunadavanya.data.local.WildlifeDao
import com.karunadavanya.data.remote.AlertRemoteDataSource
import com.karunadavanya.data.remote.FirebaseRealtimeAlertDataSource
import com.karunadavanya.data.repository.AlertRepository
import com.karunadavanya.data.repository.SightingReportRepository
import com.karunadavanya.data.repository.WildlifeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KarunadaVanyaDatabase {
        return KarunadaVanyaDatabase.getDatabase(context)
    }

    @Provides
    fun provideWildlifeDao(database: KarunadaVanyaDatabase): WildlifeDao = database.wildlifeDao()

    @Provides
    fun provideAlertDao(database: KarunadaVanyaDatabase): AlertDao = database.alertDao()

    @Provides
    fun provideSightingReportDao(database: KarunadaVanyaDatabase): SightingReportDao {
        return database.sightingReportDao()
    }

    @Provides
    @Singleton
    fun provideAlertRemoteDataSource(): AlertRemoteDataSource = FirebaseRealtimeAlertDataSource()

    @Provides
    @Singleton
    fun provideWildlifeRepository(wildlifeDao: WildlifeDao): WildlifeRepository {
        return WildlifeRepository(wildlifeDao)
    }

    @Provides
    @Singleton
    fun provideAlertRepository(
        alertDao: AlertDao,
        remoteDataSource: AlertRemoteDataSource
    ): AlertRepository {
        return AlertRepository(alertDao, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideSightingReportRepository(
        sightingReportDao: SightingReportDao,
        alertDao: AlertDao,
        remoteDataSource: AlertRemoteDataSource
    ): SightingReportRepository {
        return SightingReportRepository(sightingReportDao, alertDao, remoteDataSource)
    }
}
