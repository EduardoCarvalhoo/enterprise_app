package com.example.appioasys.data.repository

import com.example.appioasys.data.response.LoginResult
import com.example.appioasys.domain.model.User

interface LoginRepository {
    fun getAuthenticationData(user: User, loginResultCallback: (result: LoginResult) -> Unit)
}