package com.dicoding.asclepius.view.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.data.repository.PredictionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel(private val predictionRepository: PredictionRepository): ViewModel() {
    fun savePrediction(predictionHistory: PredictionHistoryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            predictionRepository.savePredictionHistory(predictionHistory)
        }
    }
}