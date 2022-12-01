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
    private val viewModel by lazy {
        LoginViewModel.LoginViewModelFactory(CompanyApiAuthenticationDataSource())
            .create(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.resetLiveData()
        window.statusBarColor = ContextCompat.getColor(this, R.color.custom_black_23)
        setupClickListeners()
        setupObserver()
        resetViewsAfterTextChanged()
        navigateToHomeScreen()
    }

    private fun setupClickListeners() {
        with(binding) {
            loginEnterButton.setOnClickListener {
                val user = User(
                    email = loginEmailEditText.text?.toString().orEmpty(),
                    password = loginPasswordEditText.text?.toString().orEmpty()
                )
                viewModel.doLogin(user)
            }
        }
    }

    private fun setupObserver() {
        viewModel.emailErrorMessageLiveData.observe(this) { errorMessageId ->
            errorMessageId?.let {
                binding.loginEmailFieldTextInputLayout.error = getString(it)
            }
        }
        viewModel.passwordErrorMessageLiveData.observe(this) { errorMessageId ->
            errorMessageId?.let {
                binding.loginPasswordFieldTextInputLayout.error = getString(errorMessageId)
            }
        }
        viewModel.loginServerErrorLiveData.observe(this) { codeError ->
            showAlertDialog(codeError)
        }
        viewModel.isLoadingLiveData.observe(this) { isLoading ->
            binding.loginProgressBar.isVisible = isLoading
        }
        viewModel.blockLoginLiveData.observe(this) {
            binding.loginEnterButton.isEnabled = false
        }
        viewModel.showUnauthorizedErrorLiveData.observe(this) {
            with(binding) {
                loginPasswordFieldTextInputLayout.error =
                    getString(R.string.login_invalid_error_message)
                loginEmailFieldTextInputLayout.error = EMPTY_ERROR_MESSAGE
                loginEnterButton.isEnabled = false
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
        viewModel.loginSuccessLiveData.observe(this) { authenticationData ->
            authenticationData?.let {
                binding.loginProgressBar.isVisible = false
                val intent = HomeActivity.getStartIntent(
                    this@LoginActivity, authenticationData
                )
                startActivity(intent)
            }
        }
    }

    companion object {
        const val EMPTY_ERROR_MESSAGE = ""
    }
}
