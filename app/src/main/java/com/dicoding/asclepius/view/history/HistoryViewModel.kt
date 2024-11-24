package com.dicoding.asclepius.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.data.repository.PredictionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(private val predictionRepository: PredictionRepository) : ViewModel() {
    private val _predictionHistory = MutableLiveData<List<PredictionHistoryEntity>>()
    val predictionHistory: LiveData<List<PredictionHistoryEntity>> = _predictionHistory
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getHistory(): LiveData<List<PredictionHistoryEntity>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                predictionRepository.getPredictionHistory().asFlow()
                    .collect { history ->
                        val predictions = history.map { prediction ->
                            PredictionHistoryEntity(
                                prediction.image,
                                prediction.result,
                                prediction.confidenceScore
                            )
                        }
                        withContext(Dispatchers.Main) {
                            _predictionHistory.value = predictions
                        }
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _predictionHistory.value = emptyList()
                    _toastMessage.value = e.message
                }
            }
        }
        return predictionHistory
    }

    fun deleteHistory() {
        viewModelScope.launch {
            predictionRepository.clearHistory()
        }
    }
}