package com.dicoding.asclepius.view.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import java.util.Locale

class HistoryAdapter : ListAdapter<PredictionHistoryEntity, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(predictionHistory: PredictionHistoryEntity) {
            val imageUri = Uri.parse(predictionHistory.image)
            binding.ivImageUri.setImageURI(imageUri)
            binding.tvResult.text = predictionHistory.result
            binding.tvScore.text = String.format(Locale.ENGLISH, "%.2f", predictionHistory.confidenceScore)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PredictionHistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: PredictionHistoryEntity,
                newItem: PredictionHistoryEntity
            ): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(
                oldItem: PredictionHistoryEntity,
                newItem: PredictionHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}