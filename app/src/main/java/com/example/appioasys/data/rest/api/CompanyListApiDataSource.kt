package com.example.appioasys.data.rest.api

import com.example.appioasys.data.repository.HomeRepository
import com.example.appioasys.data.response.CompanyListResponse
import com.example.appioasys.data.response.HomeListResult
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.data.rest.retrofit.RetrofitConfig
import com.example.appioasys.domain.model.toItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyListApiDataSource : HomeRepository {

    override fun getCompanyList(
        authenticationUser: LoginAuthenticationUser,
        newText: String?,
        companyListResultCallback: (result: HomeListResult) -> Unit
    ) {
        val callList: Call<CompanyListResponse> = RetrofitConfig.homeService.getEnterpriseList(
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
                    else -> companyListResultCallback(HomeListResult.ApiError(response.code()))
                }
            }

            override fun onFailure(call: Call<CompanyListResponse>, t: Throwable) {
                companyListResultCallback(HomeListResult.SeverError(t))
            }
        })
    }
}