package com.example.appioasys.data.rest.service

import com.example.appioasys.data.response.CompanyListResponse
import retrofit2.Call
import retrofit2.http.*

interface HomeService {

    @GET("enterprises")
    fun getEnterpriseList(
        @Header("access-token") accessToken: String,
        @Header("client") client: String,
        @Header("uid") uid: String,
        @Query("name") name: String
    ): Call<CompanyListResponse>
}