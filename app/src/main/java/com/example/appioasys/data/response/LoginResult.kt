package com.example.appioasys.data.response

sealed class LoginResult{
    data class Success(val data: LoginAuthenticationUser): LoginResult()
    class ApiError(val error: Int): LoginResult()
    class ServerError(val serverError: Throwable): LoginResult()
}