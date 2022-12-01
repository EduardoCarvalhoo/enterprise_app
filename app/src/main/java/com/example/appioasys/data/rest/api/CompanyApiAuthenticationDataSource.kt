package com.example.appioasys.data.rest.api

import com.example.appioasys.data.repository.LoginRepository
import com.example.appioasys.data.response.LoginAuthenticationUser
import com.example.appioasys.data.response.LoginRequest
import com.example.appioasys.data.response.LoginResult
import com.example.appioasys.data.rest.retrofit.RetrofitConfig
import com.example.appioasys.domain.model.NoInternetException
import com.example.appioasys.domain.model.ServerException
import com.example.appioasys.domain.model.UnauthorizedException
import com.example.appioasys.domain.model.User
import com.example.appioasys.utils.CLIENT
import com.example.appioasys.utils.TOKEN
import com.example.appioasys.utils.UID
import okhttp3.Headers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class CompanyApiAuthenticationDataSource : LoginRepository {

    override fun getAuthenticationData(
        user: User,
        loginResultCallback: (result: LoginResult) -> Unit
    ) {
        val companyService =
            RetrofitConfig.loginService.login(LoginRequest(user.email, user.password))
        companyService.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                when {
                    response.isSuccessful -> {
                        val headers: Headers = response.headers()
                        val token = headers[TOKEN]
                        val client = headers[CLIENT]
                        val uid = headers[UID]

                        val resultData = LoginAuthenticationUser(token, client, uid)
                        loginResultCallback(LoginResult.Success(resultData))
                    }
                    response.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        loginResultCallback(LoginResult.Error(UnauthorizedException))
                    }
                    else -> {
                        loginResultCallback(LoginResult.Error(ServerException))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loginResultCallback(
                    LoginResult.Error(
                        if (t is IOException) NoInternetException else ServerException
                    )
                )
            }
        })
    }
}