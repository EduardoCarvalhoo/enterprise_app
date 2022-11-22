package com.example.appioasys.data.response
import java.io.Serializable

data class LoginAuthenticationUser(
    val token: String?,
    val client: String?,
    val uid: String?
): Serializable