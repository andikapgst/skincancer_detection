package com.dicoding.asclepius.view.news

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repository.PredictionRepository

class NewsViewModel(private val predictionRepository: PredictionRepository) : ViewModel() {
    fun getHeadlineNews() = predictionRepository.getCancerNews()
}