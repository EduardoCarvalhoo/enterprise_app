package com.example.appioasys.data.rest.api

import com.example.appioasys.data.repository.CompanyListRepository
import com.example.appioasys.data.response.CompanyListResponse
import com.example.appioasys.data.response.HomeListResult
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.data.rest.service.HomeService
import com.example.appioasys.domain.model.NoInternetException
import com.example.appioasys.domain.model.ServerException
import com.example.appioasys.domain.model.UnauthorizedException
import com.example.appioasys.domain.model.toItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class CompanyListApiDataSource(private val service: HomeService) : CompanyListRepository {

    override fun getCompanyList(
        authenticationUser: LoginAuthenticationUser,
        newText: String?,
        companyListResultCallback: (result: HomeListResult) -> Unit
    ) {
        val callList = service.getEnterpriseList(
            authenticationUser.token.orEmpty(),
            authenticationUser.client.orEmpty(),
            authenticationUser.uid.orEmpty(),
            newText.orEmpty()
        )
        callList.enqueue(object : Callback<CompanyListResponse> {
            override fun onResponse(
                call: Call<CompanyListResponse>, response: Response<CompanyListResponse>
            ) {
                when {
                    response.isSuccessful -> {
                        val companyList = response.body()?.companies.toItem()
                        companyListResultCallback(HomeListResult.Success(companyList))
                    }
                    response.code() == HttpURLConnection.HTTP_UNAUTHORIZED ->
                        companyListResultCallback(HomeListResult.Error(UnauthorizedException))

                    else -> companyListResultCallback(HomeListResult.Error(ServerException))
                }
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                companyListResultCallback(
                    HomeListResult.Error(
                        if (t is IOException) NoInternetException else ServerException)
                )
            }
        })
    }
}