package com.capstone.signmate_c241_ps262.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
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
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)

        Log.d("ImageClassifierHelper", "Input Buffer Size: ${inputBuffer.capacity()}")
        inputBuffer.rewind()

        if (inputBuffer.remaining() < 128 * 128 * 3 * 4) {
            Log.e("ImageClassifierHelper", "Buffer underflow: Not enough data in the buffer")
            return FloatArray(26)
        }

        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, OUTPUT_SIZE), org.tensorflow.lite.DataType.FLOAT32)
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        Log.d("ImageClassifierHelper", "Output Buffer: ${outputBuffer.floatArray.joinToString()}")

        return outputBuffer.floatArray
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(128 * 128 * 3 * 4)
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
