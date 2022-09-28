package com.example.appioasys.utils

import com.example.appioasys.data.response.CompanyItemResponse
import com.example.appioasys.domain.model.CompanyItemMapped

fun List<CompanyItemResponse>?.toItem(): List<CompanyItemMapped>? = this?.map { companyItemResponse ->
    CompanyItemMapped(
        companyItemResponse.companyName,
        companyItemResponse.photoUrl,
        companyItemResponse.companyDescription,
        companyItemResponse.companyCity,
        companyItemResponse.kindOfService.enterprise_type_name
    )
}