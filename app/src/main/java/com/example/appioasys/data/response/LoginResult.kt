package com.example.appioasys.data.response

sealed class LoginResult{
    data class Success(val data: LoginAuthenticationUser): LoginResult()
    data class Error(val throwable: Throwable): LoginResult()
}