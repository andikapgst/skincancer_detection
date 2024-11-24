package com.dicoding.asclepius.view.home

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            binding?.previewImageView?.setImageURI(uri)
        }

        binding?.galleryButton?.setOnClickListener { startGallery() }
        binding?.analyzeButton?.setOnClickListener {
            viewModel.currentImageUri.value?.let { uri ->
                val timestamp = System.currentTimeMillis()
                val fileName = "cropped_$timestamp.jpg"
                val uCrop = UCrop.of(uri, Uri.fromFile(File(context?.cacheDir, fileName)))
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(maxWidth, maxHeight)
                uCrop.start(requireContext(), this)
            } ?: run {
                showToast()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setCurrentImageUri(uri)
        } else {
            showToast()
        }
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = data?.let {
                UCrop.getOutput(it)
            }
            if (resultUri != null) {
                viewModel.setCurrentImageUri(resultUri)
            }
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.putExtra(ResultActivity.CROPPED_IMAGE_URI, resultUri)
            startActivity(intent)
        } else if (
            requestCode == UCrop.REQUEST_CROP && resultCode == UCrop.RESULT_ERROR) {
            val error = data?.let {
                UCrop.getError(it) }
            Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast() {
        Toast.makeText(context, "No image selected.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}