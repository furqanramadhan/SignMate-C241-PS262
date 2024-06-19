package com.capstone.signmate_c241_ps262.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

@Suppress("DEPRECATION")
class ImageClassifierHelper(context: Context, modelPath: String) {

    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context, modelPath))
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classifyImage(bitmap: Bitmap): FloatArray {
        // Resize the bitmap to the expected dimensions
        val resizedBitmap = createScaledBitmap(bitmap, 128, 128, true)

        // Convert the bitmap to ByteBuffer
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)

        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, OUTPUT_SIZE), org.tensorflow.lite.DataType.FLOAT32)
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        return outputBuffer.floatArray
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(128 * 128 * 3 * 4) // 4 bytes for FLOAT32, 128x128 image, 3 channels
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(128 * 128)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until 128) {
            for (j in 0 until 128) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat((value and 0xFF) / 255.0f)
            }
        }

        return byteBuffer
    }

    companion object {
        const val OUTPUT_SIZE = 26
    }
}