package com.example.burgerkingpredict

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.burgerkingpredict.databinding.FragmentSimulationBinding

class SimulationFragment : Fragment() {
    private lateinit var binding: FragmentSimulationBinding
    private val TAG = "SimulationFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSimulationBinding.inflate(inflater, container, false)

        binding.predictButton.setOnClickListener {
            try {
                val inputs = FloatArray(12)
                val fields = listOf(
                    binding.inputCalories,
                    binding.inputFatCalories,
                    binding.inputTotalFat,
                    binding.inputTransFat,
                    binding.inputCholesterol,
                    binding.inputSodium,
                    binding.inputCarbs,
                    binding.inputFiber,
                    binding.inputSugars,
                    binding.inputProtein,
                    binding.inputWeightWatchers,
                    binding.inputServingSize
                )

                // Validasi dan parsing input
                for (i in fields.indices) {
                    val value = fields[i].text.toString().toFloatOrNull()
                    if (value == null) {
                        Toast.makeText(requireContext(), "Mohon isi semua input dengan benar", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    inputs[i] = value
                }

                // Prediksi dari model
                val modelHelper = TFLiteModelHelper(requireContext())
                val result = modelHelper.predict(inputs)

                Log.d(TAG, "Output Model: ${result.joinToString()}")

                if (result.isNotEmpty()) {
                    val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1
                    val confidence = result[maxIndex]

                    binding.resultText.text = "Prediksi: Kategori ke-$maxIndex\nKepercayaan: ${"%.2f".format(confidence * 100)}%"
                } else {
                    binding.resultText.text = "Prediksi gagal: hasil kosong."
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Error saat prediksi: ${e.message}")
                Toast.makeText(requireContext(), "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
}
