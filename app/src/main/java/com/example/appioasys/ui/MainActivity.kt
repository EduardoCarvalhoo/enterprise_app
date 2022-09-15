package com.example.appioasys.ui

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.appioasys.R
import com.example.appioasys.databinding.ActivityMainBinding
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainProgressBar.isVisible = false
        binding.mainEnterButton.setOnClickListener {
            validateEmail()
            validatePassword()
        }
    }

    private fun validateEmail() {
        val emailText = binding.mainEmailFieldEditText.editText?.text.toString()
        if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && validatePassword()) {
            with(binding) {
                mainEmailErrorAlertTextView.isVisible = false
                mainPasswordErrorAlertTextView.isVisible = false
                mainErrorMessageTextView.isVisible = false
                mainAccessDigitsText.isEndIconVisible = true
                mainEnterButton.setBackgroundResource(R.color.greeny_blue)
                mainProgressBar.isVisible = true
            }
        } else {
            with(binding) {
                mainEmailErrorAlertTextView.isVisible = true
                mainPasswordErrorAlertTextView.isVisible = true
                mainErrorMessageTextView.isVisible = true
                mainAccessDigitsText.isEndIconVisible = false
                mainEnterButton.setBackgroundResource(R.color.steel_grey)
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = binding.mainAccessDigitsText.editText?.text.toString()
        val regex = "^(?=.*[a-z])(?=." + "*[A-Z])(?=.*\\d)" + "(?=.*[-+_!@#$%^&*.,?]).+$"
        val patterns: Pattern = Pattern.compile(regex)
        val matcher: Matcher = patterns.matcher(password)
        return matcher.matches()
    }
}



