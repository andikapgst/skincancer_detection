package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HistoryViewModel by viewModels {
            factory
        }

        val historyAdapter = HistoryAdapter()
        viewModel.getHistory().observe(viewLifecycleOwner) { historyList ->
            binding?.fabDelete?.visibility = if (historyList.isNotEmpty()) View.VISIBLE else View.GONE
            binding?.tvEmpty?.visibility = if (historyList.isEmpty()) View.VISIBLE else View.GONE
            historyAdapter.submitList(historyList)
        }

        binding?.rvHistory?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = historyAdapter

        }

        binding?.fabDelete?.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Clear All")
                .setMessage("Clear all prediction history?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteHistory()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}