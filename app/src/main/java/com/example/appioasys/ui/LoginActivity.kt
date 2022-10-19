package com.example.appioasys.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.appioasys.R
import com.example.appioasys.data.rest.LoginRequest
import com.example.appioasys.data.rest.RetrofitConfig
import com.example.appioasys.databinding.ActivityLoginBinding
import com.example.appioasys.domain.model.FieldStatus
import com.example.appioasys.domain.model.User
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

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_black_23)
        setupClickListeners()
        resetViewsAfterTextChanged()
    }

    private fun setupClickListeners() {
        with(binding) {
            loginEnterButton.setOnClickListener {
                user = User(
                    email = loginEmailEditText.text.toString(),
                    password = loginPasswordEditText.text.toString()
                )
                val isValidEmail = validateEmail(user)
                val isValidPassword = validatePassword(user)
                if (isValidEmail && isValidPassword) {
                    doLogin(user)
                } else {
                    loginEnterButton.isEnabled = false
                }
            }
        }
    }

    private fun validateEmail(user: User): Boolean {
        return when (user.validateEmail()) {
            FieldStatus.VALID -> true
            FieldStatus.INVALID -> {
                binding.loginEmailFieldTextInputLayout.error =
                    getString(R.string.login_invalid_email_error_message)
                false
            }
            FieldStatus.BLANK -> {
                binding.loginEmailFieldTextInputLayout.error =
                    getString(R.string.login_empty_field_error_message)
                false
            }
        }
    }

    private fun validatePassword(user: User): Boolean {
        return when (user.validatePassword()) {
            FieldStatus.VALID -> true
            FieldStatus.INVALID -> {
                binding.loginPasswordFieldTextInputLayout.error =
                    getString(R.string.login_invalid_password_error_message)
                false
            }
            FieldStatus.BLANK -> {
                binding.loginPasswordFieldTextInputLayout.error =
                    getString(R.string.login_empty_field_error_message)
                false
            }
        }
    }

    private fun doLogin(user: User) {
        binding.loginProgressBar.isVisible = true
        val companyService =
            RetrofitConfig.getRetrofit().login(LoginRequest(user.email, user.password))
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
                        binding.loginPasswordFieldTextInputLayout.error =
                            getString(R.string.login_invalid_error_message)
                        binding.loginEmailFieldTextInputLayout.error = EMPTY_ERROR_MESSAGE
                        binding.loginEnterButton.isEnabled = false
                    }
                    else -> showAlertDialog(getString(R.string.generic_error_text)) {
                        doLogin(user)
                    }
                }
                binding.loginProgressBar.isVisible = false
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                binding.loginProgressBar.isVisible = false
                handleLoginDataFailure(t)
            }
        })
    }

    private fun handleLoginDataFailure(throwable: Throwable) {
        if (throwable is IOException) {
            showAlertDialog(getString(R.string.no_internet_connection_error_text)) {
                doLogin(user)
            }
        } else {
            showAlertDialog(getString(R.string.generic_error_text)) {
                doLogin(user)
            }
        }
    }

    private fun resetViewsAfterTextChanged() {
        with(binding) {
            loginEmailEditText.doAfterTextChanged {
                loginEnterButton.isEnabled = true
                loginEmailFieldTextInputLayout.error = null
            }
            loginPasswordEditText.doAfterTextChanged {
                loginEnterButton.isEnabled = true
                loginPasswordFieldTextInputLayout.error = null
            }
        }
    }

    private fun navigateToHomeScreen(token: String?, client: String?, uid: String?) {
        val intentHome = Intent(this, HomeActivity::class.java)
        intentHome.putExtra(TOKEN, token)
        intentHome.putExtra(CLIENT, client)
        intentHome.putExtra(UID, uid)
        startActivity(intentHome)
    }

    private companion object {
        const val EMPTY_ERROR_MESSAGE = " "
    }
}
