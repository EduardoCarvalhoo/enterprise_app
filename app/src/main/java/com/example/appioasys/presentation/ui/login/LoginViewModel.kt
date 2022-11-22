package com.example.appioasys.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appioasys.R
import com.example.appioasys.data.repository.LoginRepository
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.data.response.LoginResult
import com.example.appioasys.domain.model.FieldStatus
import com.example.appioasys.domain.model.User
import java.io.IOException
import java.net.HttpURLConnection

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val loginMutableLiveData = MutableLiveData<LoginAuthenticationUser>()
    val loginLiveData: LiveData<LoginAuthenticationUser> get() = loginMutableLiveData
    private val loginErrorMutableLiveData = MutableLiveData<Int>()
    val loginErrorLiveData: LiveData<Int> get() = loginErrorMutableLiveData
    private val loginServerErrorMutableLiveData = MutableLiveData<Int>()
    val loginServerErrorLiveData: LiveData<Int> get() = loginServerErrorMutableLiveData
    private val loginValidateEmailMutableLiveData = MutableLiveData<Int>()
    val loginValidateEmailLiveData: LiveData<Int> get() = loginValidateEmailMutableLiveData
    private val loginValidatePasswordMutableLiveData = MutableLiveData<Int>()
    val loginValidatePasswordLiveData: LiveData<Int> get() = loginValidatePasswordMutableLiveData

    fun doLogin(user: User) {
        loginRepository.getAuthenticationData(user) { result: LoginResult ->
            when (result) {
                is LoginResult.Success -> {
                    loginMutableLiveData.postValue(result.data)
                }
                is LoginResult.ApiError -> {
                    if (result.error == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        loginErrorMutableLiveData.postValue(result.error)
                    }
                }
                is LoginResult.ServerError -> {
                    if (result.serverError is IOException) {
                        loginServerErrorMutableLiveData.postValue(R.string.no_internet_connection_error_text)
                    } else {
                        loginServerErrorMutableLiveData.postValue(R.string.generic_error_text)
                    }
                }
            }
        }
    }

    fun validateEmail(user: User): Boolean {
        return when (user.validateEmail()) {
            FieldStatus.VALID -> true
            FieldStatus.INVALID -> {
                loginValidateEmailMutableLiveData.postValue(R.string.login_invalid_email_error_message)
                false
            }
            FieldStatus.BLANK -> {
                loginValidateEmailMutableLiveData.postValue(R.string.login_empty_field_error_message)
                false
            }
        }
    }

    fun validatePassword(user: User): Boolean {
        return when (user.validatePassword()) {
            FieldStatus.VALID -> true
            FieldStatus.INVALID -> {
                loginValidatePasswordMutableLiveData.postValue(R.string.login_invalid_password_error_message)
                false
            }
            FieldStatus.BLANK -> {
                loginValidatePasswordMutableLiveData.postValue(R.string.login_empty_field_error_message)
                false
            }
        }
    }

    class LoginViewModelFactory(private val loginRepository: LoginRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(loginRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}