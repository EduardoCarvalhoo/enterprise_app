package com.example.appioasys.presentation.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.appioasys.R
import com.example.appioasys.data.rest.api.CompanyApiAuthenticationDataSource
import com.example.appioasys.databinding.ActivityLoginBinding
import com.example.appioasys.domain.model.User
import com.example.appioasys.presentation.ui.home.HomeActivity
import com.example.appioasys.utils.showAlertDialog

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var user: User
    private val viewModel by lazy {
        LoginViewModel.LoginViewModelFactory(CompanyApiAuthenticationDataSource())
            .create(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_black_23)
        setupClickListeners()
        setupObserver()
        resetViewsAfterTextChanged()
        navigateToHomeScreen()
    }

    private fun setupClickListeners() {
        with(binding) {
            loginEnterButton.setOnClickListener {
                user = User(
                    email = loginEmailEditText.text.toString(),
                    password = loginPasswordEditText.text.toString()
                )
                if (viewModel.validateEmail(user) && viewModel.validatePassword(user)) {
                    viewModel.doLogin(user)
                    loginProgressBar.isVisible = true
                } else {
                    loginEnterButton.isEnabled = false
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.loginValidateEmailLiveData.observe(this) {
            binding.loginEmailFieldTextInputLayout.error = getString(it)
        }
        viewModel.loginValidatePasswordLiveData.observe(this) {
            binding.loginPasswordFieldTextInputLayout.error = getString(it)
        }
        viewModel.loginServerErrorLiveData.observe(this) { codeError ->
            showAlertDialog(codeError)
            binding.loginProgressBar.isVisible = false
        }
        viewModel.loginErrorLiveData.observe(this) {
            with(binding) {
                loginPasswordFieldTextInputLayout.error =
                    getString(R.string.login_invalid_error_message)
                loginEmailFieldTextInputLayout.error = EMPTY_ERROR_MESSAGE
                loginEnterButton.isEnabled = false
                loginProgressBar.isVisible = false
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

    private fun navigateToHomeScreen() {
        viewModel.loginLiveData.observe(this) { authenticationData ->
            binding.loginProgressBar.isVisible = false
            val intent = HomeActivity.getStartIntent(
                this@LoginActivity, authenticationData
            )
            startActivity(intent)
        }
    }

    companion object {
        const val EMPTY_ERROR_MESSAGE = ""
    }
}
