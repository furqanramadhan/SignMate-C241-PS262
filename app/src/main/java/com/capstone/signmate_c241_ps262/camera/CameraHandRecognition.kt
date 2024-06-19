package com.capstone.signmate_c241_ps262.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.signmate_c241_ps262.databinding.ActivityCameraHandRecognitionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class CameraHandRecognition : AppCompatActivity() {
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var binding: ActivityCameraHandRecognitionBinding
    private var frameCounter = 0
    private val recentPredictions = ArrayDeque<String>(5)
    private var lastProcessingTime = System.currentTimeMillis()

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val FRAME_PROCESSING_INTERVAL = 10
        private const val MIN_PROCESSING_INTERVAL = 500 // in milliseconds
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val gestureLabels = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraHandRecognitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageClassifierHelper = ImageClassifierHelper(this, "optimal_model.tflite")

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(Dispatchers.Default.asExecutor()) { image ->
                        analyzeImage(image)
                    }
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                Toast.makeText(this, "Use case binding failed", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun analyzeImage(image: ImageProxy) {
        frameCounter++
        if (frameCounter % FRAME_PROCESSING_INTERVAL != 0) {
            image.close()
            return
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastProcessingTime < MIN_PROCESSING_INTERVAL) {
            image.close()
            return
        }
        lastProcessingTime = currentTime

        val bitmap = convertImageToBitmap(image)

        lifecycleScope.launch(Dispatchers.Default) {
            val result = imageClassifierHelper.classifyImage(bitmap)
            handleOutput(result)
        }

        image.close()
    }

    private fun convertImageToBitmap(image: ImageProxy): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
        val imageBytes = out.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        // Scale bitmap to expected input size (128x128)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        return scaledBitmap
    }

    private fun handleOutput(result: FloatArray) {
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1
        val recognizedLetter = if (maxIndex != -1) gestureLabels[maxIndex] else "Unknown"

        runOnUiThread {
            Toast.makeText(this, "Recognized: $recognizedLetter", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}