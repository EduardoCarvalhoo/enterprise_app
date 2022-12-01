package com.example.appioasys.data.repository

import com.example.appioasys.data.response.HomeListResult
import com.example.appioasys.data.response.LoginAuthenticationUser

interface CompanyListRepository {
    fun getCompanyList(
        authenticationUser: LoginAuthenticationUser,
        newText: String?,
        companyListResultCallback: (result: HomeListResult) -> Unit
    )
}