package com.example.appioasys.data.response

import com.example.appioasys.domain.model.CompanyItem

sealed class HomeListResult {
    class Success(val companyListResult: List<CompanyItem>?) : HomeListResult()
    class ApiError(val statusCode: Int) : HomeListResult()
    class SeverError(val throwable: Throwable) : HomeListResult()
}