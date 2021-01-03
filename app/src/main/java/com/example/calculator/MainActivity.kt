package com.example.calculator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isDotLast = false
    private var isOperatorLast = false
    private var isDigitLast = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    fun onDigit(view: View) {
        binding.tvInput.append((view as Button).text)
        isOperatorLast = false
        isDigitLast = true
    }

    fun onOperator(view: View) {
        val btnText = (view as Button).text

        if (btnText == "-" && !isOperatorLast) {
            binding.tvInput.append(btnText)
            isOperatorLast = true
            isDotLast = false
        }

        if (!isOperatorLast && binding.tvInput.text.toString().isNotEmpty()) {
            binding.tvInput.append(btnText)
            isOperatorLast = true
            isDigitLast = false
            isDotLast = false
        }
    }

    fun onClear(view: View) {
        binding.tvInput.text = ""
        isDotLast = false
        isDigitLast = false
        isOperatorLast = false
    }

    fun onDecimal(view: View) {
        if (!isDotLast) {
            binding.tvInput.append((view as Button).text)
            isDotLast = true
            isOperatorLast = false
        }
    }

    fun onDelete(view: View) {
        val errorMessage = getString(R.string.error_message)
        var inputText = binding.tvInput.text.toString()

        when {
            inputText == errorMessage -> {
                onClear(view)
            }

            inputText.isNotEmpty() -> {
                inputText = inputText.substring(0, inputText.length - 1)
                binding.tvInput.text = inputText
                isOperatorLast = false
            }
        }
    }

    fun onPercentage(view: View) {
        if (isDigitLast) {
            binding.tvInput.append((view as Button).text)
            isDigitLast = false
        }
    }

    fun onSqrt(view: View) = binding.tvInput.append("^(1/2)")

    fun onEqual(view: View) {
        if (binding.tvInput.text.isNotEmpty()) {
            val e = Expression()
            val result: Double

            e.expressionString = formatString()
            result = e.calculate()

            when {
                result.toString() == "NaN" -> {
                    binding.tvInput.text = getString(R.string.error_message)
                }
                result % 1 == 0.0 -> {
                    binding.tvInput.text = result.toInt().toString()
                }
                else -> {
                    binding.tvInput.text = result.toString()
                }
            }
        }
    }

    private fun formatString(): String {
        var inputText = binding.tvInput.text.toString()

        inputText = inputText.replace('×', '*').replace('÷', '/')
        return inputText
    }
}
