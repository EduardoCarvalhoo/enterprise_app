package com.example.appioasys.di

import com.example.appioasys.data.repository.LoginRepository
import com.example.appioasys.data.rest.api.CompanyApiAuthenticationDataSource
import com.example.appioasys.data.rest.service.LoginService
import com.example.appioasys.presentation.ui.login.LoginViewModel
import com.example.appioasys.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val loginModule = module {
    single<LoginService> {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginService::class.java)
    }
}

val appLoginModule = module {
    single<LoginRepository> { CompanyApiAuthenticationDataSource(get()) }
    viewModel { LoginViewModel(loginRepository = get()) }
}





