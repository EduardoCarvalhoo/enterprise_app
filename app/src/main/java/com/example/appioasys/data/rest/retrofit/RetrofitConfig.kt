package com.example.appioasys.data.rest.retrofit

import com.example.appioasys.data.rest.service.HomeService
import com.example.appioasys.data.rest.service.LoginService
import com.example.appioasys.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val loginService: LoginService = initRetrofit().create(LoginService::class.java)
    val homeService: HomeService = initRetrofit().create(HomeService::class.java)
}