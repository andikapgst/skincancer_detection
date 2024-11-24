package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity

@Dao
interface PredictionHistoryDao {
    @Query("SELECT * FROM PredictionHistoryEntity")
    fun getAllPredictions(): LiveData<List<PredictionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPrediction(prediction: PredictionHistoryEntity)

    @Query("DELETE FROM PredictionHistoryEntity")
    suspend fun deleteAll()
}