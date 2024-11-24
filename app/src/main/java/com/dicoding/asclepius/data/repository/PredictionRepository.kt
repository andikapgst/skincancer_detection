package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.data.local.room.PredictionHistoryDao
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.retrofit.ApiService

class PredictionRepository private constructor(
    private val apiService: ApiService,
    private val predictionHistoryDao: PredictionHistoryDao
) {
    fun getPredictionHistory(): LiveData<List<PredictionHistoryEntity>> {
        return predictionHistoryDao.getAllPredictions()
    }

    suspend fun savePredictionHistory(predictionHistory: PredictionHistoryEntity) {
        predictionHistoryDao.insertPrediction(predictionHistory)
    }

    suspend fun clearHistory() {
        predictionHistoryDao.deleteAll()
    }

    fun getCancerNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getCancerNews(BuildConfig.API_KEY)
            val articles = response.articles
            val articleList = ArrayList<ArticlesItem>()
            for (article in articles) {
                article.title
                article.publishedAt
                article.urlToImage
                article.url
                articleList.add(article)
            }
            emit(Result.Success(articleList))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: PredictionRepository? = null
        fun getInstance(
            apiService: ApiService,
            predictionHistoryDao: PredictionHistoryDao
        ): PredictionRepository =
            instance ?: synchronized(this) {
                instance ?: PredictionRepository(apiService, predictionHistoryDao)
            }.also { instance = it }
    }
}