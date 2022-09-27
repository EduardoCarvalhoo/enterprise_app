package com.example.appioasys.api

import com.example.appioasys.utils.LOGIN_URL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CompanyService {

    @POST(LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<ResponseBody>
}