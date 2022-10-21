package com.example.appioasys.data.rest

import com.example.appioasys.data.response.CompanyListResponse
import com.example.appioasys.utils.LOGIN_URL
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CompanyService {

    @POST(LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<ResponseBody>

    @GET("enterprises")
    fun getEnterpriseList(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("uid") uid: String,
        @Query("name") name: String
    ): Call<CompanyListResponse>
}