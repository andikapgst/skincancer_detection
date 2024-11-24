package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.data.local.entity.PredictionHistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat
import kotlin.getValue

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Result"

        @Suppress("DEPRECATION")
        val croppedImageUri = intent.getParcelableExtra<Uri>(CROPPED_IMAGE_URI)
        croppedImageUri?.let {
            binding.resultImage.setImageURI(it)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        runOnUiThread {
                            Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onResults(
                        results: List<Classifications>?
                    ) {
                        runOnUiThread {
                            results?.let { it ->
                                if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                    println(it)
                                    val sortedCategories =
                                        it[0].categories.sortedByDescending { it?.score }
                                    val displayResult =
                                        sortedCategories.joinToString("\n") {
                                            "${it?.label} " + NumberFormat.getPercentInstance()
                                                .format(it.score).trim()
                                        }
                                    binding.resultText.text = displayResult

                                    val image = croppedImageUri
                                    val result = sortedCategories[0].label
                                    val score = sortedCategories[0].score
                                    val prediction = PredictionHistoryEntity(
                                        image = image.toString(),
                                        result = result,
                                        confidenceScore = score
                                    )
                                    viewModel.savePrediction(prediction)
                                } else {
                                    binding.resultText.text = ""
                                    Toast.makeText(this@ResultActivity, "Failed to classify image", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(croppedImageUri)
        }
    }

    companion object {
        const val CROPPED_IMAGE_URI = "cropped_image_uri"
    }
}