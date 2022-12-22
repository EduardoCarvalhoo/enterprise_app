package com.example.appioasys.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appioasys.R
import com.example.appioasys.data.repository.LoginRepository
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.data.response.LoginResult
import com.example.appioasys.domain.model.FieldStatus
import com.example.appioasys.domain.model.NoInternetException
import com.example.appioasys.domain.model.UnauthorizedException
import com.example.appioasys.domain.model.User

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val loginSuccessMutableLiveData = MutableLiveData<LoginAuthenticationUser?>()
    val loginSuccessLiveData: LiveData<LoginAuthenticationUser?> = loginSuccessMutableLiveData
    private val showUnauthorizedErrorMutableLiveData = MutableLiveData<Unit>()
    val showUnauthorizedErrorLiveData: LiveData<Unit> = showUnauthorizedErrorMutableLiveData
    private val loginServerErrorMutableLiveData = MutableLiveData<Int>()
    val loginServerErrorLiveData: LiveData<Int> = loginServerErrorMutableLiveData
    private val emailErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val emailErrorMessageLiveData: LiveData<Int?> = emailErrorMessageMutableLiveData
    private val passwordErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val passwordErrorMessageLiveData: LiveData<Int?> = passwordErrorMessageMutableLiveData
    private val isLoadingMutableLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = isLoadingMutableLiveData
    private val blockLoginMutableLiveData = MutableLiveData<Unit>()
    val blockLoginLiveData: LiveData<Unit> = blockLoginMutableLiveData

    fun doLogin(user: User) {
        val isValidEmail = validateEmail(user)
        val isValidPassword = validatePassword(user)
        if (isValidEmail && isValidPassword) {
            isLoadingMutableLiveData.postValue(true)
            loginRepository.getAuthenticationData(user) { result: LoginResult ->
                when (result) {
                    is LoginResult.Success -> {
                        loginSuccessMutableLiveData.postValue(result.data)
                    }
                    is LoginResult.Error -> {
                        when (result.throwable) {
                            is UnauthorizedException -> {
                                showUnauthorizedErrorMutableLiveData.postValue(Unit)
                            }
                            is NoInternetException -> loginServerErrorMutableLiveData.postValue(R.string.no_internet_connection_error_text)
                            else -> loginServerErrorMutableLiveData.postValue(R.string.generic_error_text)
                        }
                    }
                }
                isLoadingMutableLiveData.postValue(false)
            }
        } else {
            blockLoginMutableLiveData.postValue(Unit)
        }
    }

    fun resetLiveData(){
        loginSuccessMutableLiveData.postValue(null)
    }

    private fun validateEmail(user: User): Boolean {
        return when (user.validateEmail()) {
            FieldStatus.VALID -> {
                emailErrorMessageMutableLiveData.postValue(null)
                true
            }
            FieldStatus.INVALID -> {
                emailErrorMessageMutableLiveData.postValue(R.string.login_invalid_email_error_message)
                false
            }
            FieldStatus.BLANK -> {
                emailErrorMessageMutableLiveData.postValue(R.string.login_empty_field_error_message)
                false
            }
        }
    }

    private fun validatePassword(user: User): Boolean {
        return when (user.validatePassword()) {
            FieldStatus.VALID -> {
                passwordErrorMessageMutableLiveData.postValue(null)
                true
            }
            FieldStatus.INVALID -> {
                passwordErrorMessageMutableLiveData.postValue(R.string.login_invalid_password_error_message)
                false
            }
            FieldStatus.BLANK -> {
                passwordErrorMessageMutableLiveData.postValue(R.string.login_empty_field_error_message)
                false
            }
        }
    }
}