package com.example.appioasys.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.appioasys.R
import com.example.appioasys.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_black_23)

        validateEmail()
        configureEmailAndPasswordChange()
    }

    private fun validateEmail() {
        with(binding) {
            mainEnterButton.setOnClickListener {
                val emailText = mainEmailEditText.text.toString()
                val password = mainPasswordEditText.text.toString()
                if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && password.length > 3) {
                    mainProgressBar.isVisible = true
                    navigateToHomeScreen()
                } else {
                    mainEmailErrorAlertTextView.isVisible = true
                    mainPasswordErrorAlertTextView.isVisible = true
                    mainErrorMessageTextView.isVisible = true
                    mainPasswordFieldLayout.isEndIconVisible = false
                    mainEnterButton.setBackgroundResource(R.color.steel_grey)
                }
            }
        }
    }

        private fun configureEmailAndPasswordChange() {
            val emailText: EditText = binding.mainEmailEditText
            val passwordText: EditText = binding.mainPasswordEditText
            configureTextChange(emailText)
            configureTextChange(passwordText)
        }

        private fun configureTextChange(inputText: EditText) {
            inputText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    with(binding) {
                        mainEmailErrorAlertTextView.isVisible = false
                        mainPasswordErrorAlertTextView.isVisible = false
                        mainErrorMessageTextView.isVisible = false
                        mainPasswordFieldLayout.isEndIconVisible = true
                        mainEnterButton.setBackgroundResource(R.color.greeny_blue)
                        mainProgressBar.isVisible = false
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }

        private fun navigateToHomeScreen() {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
        }
    }



