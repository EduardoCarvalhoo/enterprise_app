package com.example.appioasys.di

import com.example.appioasys.data.repository.CompanyListRepository
import com.example.appioasys.data.rest.api.CompanyListApiDataSource
import com.example.appioasys.data.rest.service.HomeService
import com.example.appioasys.presentation.ui.home.HomeViewModel
import com.example.appioasys.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val homeModule = module {
    single<HomeService> {
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
            .create(HomeService::class.java)
    }
}

val appHomeModule = module{
    factory<CompanyListRepository> { CompanyListApiDataSource(get()) }
    viewModel { HomeViewModel(dataSource = get()) }
}