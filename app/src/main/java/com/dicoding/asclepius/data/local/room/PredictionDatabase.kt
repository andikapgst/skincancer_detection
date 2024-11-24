package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity

@Database(entities = [PredictionHistoryEntity::class], version = 1, exportSchema = false)
abstract class PredictionDatabase : RoomDatabase() {
    abstract fun predictionHistoryDao(): PredictionHistoryDao

    companion object {
        @Volatile
        private var instance: PredictionDatabase? = null
        fun getInstance(context: Context): PredictionDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PredictionDatabase::class.java,
                    "Prediction.db"
                ).build()
            }
    }
}