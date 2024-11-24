package com.dicoding.asclepius.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PredictionHistoryEntity(
    @field:PrimaryKey(autoGenerate = false)
    var image: String = "",
    var result: String = "",
    var confidenceScore: Float
)