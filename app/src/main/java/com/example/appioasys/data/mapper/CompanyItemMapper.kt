package com.example.appioasys.domain.model

import com.example.appioasys.data.response.HomeItemResponse

fun List<HomeItemResponse>?.toItem(): List<CompanyItem>? = this?.map { companyItemResponse ->
    CompanyItem(
        companyItemResponse.name,
        companyItemResponse.photoUrl,
        companyItemResponse.description,
        companyItemResponse.city,
        companyItemResponse.companyServiceResponse?.serviceType
    )
}