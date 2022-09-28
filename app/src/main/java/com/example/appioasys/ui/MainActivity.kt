package com.example.appioasys.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.appioasys.R
import com.example.appioasys.data.rest.LoginRequest
import com.example.appioasys.databinding.ActivityMainBinding
import com.example.appioasys.data.rest.RetrofitConfig
import com.example.appioasys.utils.CLIENT
import com.example.appioasys.utils.TOKEN
import com.example.appioasys.utils.UID
import com.example.appioasys.utils.showAlertDialog
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var emailText: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_black_23)
        validateLogin()
        configureEmailAndPasswordChange()
    }

    private fun validateLogin() {
        with(binding) {
            mainEnterButton.setOnClickListener {
                emailText = mainEmailEditText.text.toString()
                password = mainPasswordEditText.text.toString()
                if (Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && password.length > 3) {
                    mainProgressBar.isVisible = true
                    toDoLogin(emailText, password)
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

    private fun toDoLogin(emailText: String, password: String) {
        val companyService = RetrofitConfig.getRetrofit().login(LoginRequest(emailText, password))
        companyService.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when {
                    response.isSuccessful -> {
                        val headers: Headers = response.headers()
                        val token = headers[TOKEN]
                        val client = headers[CLIENT]
                        val uid = headers[UID]
                        navigateToHomeScreen(token, client, uid)
                    }
                    response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.main_login_error_warning_text),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.mainProgressBar.isVisible = false
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                binding.mainProgressBar.isVisible = false
                handleLoginDataFailure(t)
            }
        })
    }

    private fun handleLoginDataFailure(throwable: Throwable) {
        if (throwable is IOException) {
            showAlertDialog(getString(R.string.no_internet_connection_error_text)) {
                toDoLogin(emailText, password)
            }
        } else {
            showAlertDialog(getString(R.string.generic_error_text)) {
                toDoLogin(emailText, password)
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

    private fun navigateToHomeScreen(token: String?, client: String?, uid: String?) {
        val intentHome = Intent(this, HomeActivity::class.java)
        intentHome.putExtra(TOKEN, token)
        intentHome.putExtra(CLIENT, client)
        intentHome.putExtra(UID, uid)
        startActivity(intentHome)
    }
}



