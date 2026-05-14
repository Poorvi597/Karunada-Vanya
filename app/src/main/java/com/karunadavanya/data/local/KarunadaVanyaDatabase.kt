package com.karunadavanya.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WildlifeEntity::class, AlertEntity::class, SightingReportEntity::class],
    version = 6,
    exportSchema = false
)
abstract class KarunadaVanyaDatabase : RoomDatabase() {
    abstract fun wildlifeDao(): WildlifeDao
    abstract fun alertDao(): AlertDao
    abstract fun sightingReportDao(): SightingReportDao

    companion object {
        @Volatile
        private var instance: KarunadaVanyaDatabase? = null

        fun getDatabase(context: Context): KarunadaVanyaDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    KarunadaVanyaDatabase::class.java,
                    "karunada_vanya.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
