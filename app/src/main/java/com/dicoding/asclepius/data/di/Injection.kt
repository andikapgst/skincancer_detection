package com.dicoding.asclepius.data.di

import android.content.Context
import com.dicoding.asclepius.data.local.room.PredictionDatabase
import com.dicoding.asclepius.data.repository.PredictionRepository
import com.dicoding.asclepius.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): PredictionRepository {
        val apiService = ApiConfig.getApiService()
        val database = PredictionDatabase.getInstance(context)
        val dao = database.predictionHistoryDao()
        return PredictionRepository.getInstance(apiService, dao)
    }
}