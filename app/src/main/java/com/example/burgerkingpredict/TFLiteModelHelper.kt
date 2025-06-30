package com.example.burgerkingpredict

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TFLiteModelHelper(context: Context) {

    private var interpreter: Interpreter? = null

    init {
        try {
            val model = loadModelFile(context, "burger_king_pred.tflite") // pastikan nama file sesuai
            val options = Interpreter.Options()
            interpreter = Interpreter(model, options)
        } catch (e: IOException) {
            Log.e("TFLiteModelHelper", "Model gagal dimuat: ${e.message}")
        }
    }

    fun predict(input: FloatArray): FloatArray {
        if (interpreter == null) {
            throw IllegalStateException("Interpreter belum siap.")
        }

        // Pastikan input dalam bentuk [1, jumlah_fitur]
        val inputBuffer = arrayOf(input)
        val outputArray = Array(1) { FloatArray(5) } // 5 = jumlah kelas output (ganti sesuai model)

        try {
            interpreter?.run(inputBuffer, outputArray)
        } catch (e: Exception) {
            Log.e("TFLiteModelHelper", "Gagal prediksi: ${e.message}")
            throw e
        }

        return outputArray[0]
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context, filename: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }
}
