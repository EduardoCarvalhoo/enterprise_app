package com.example.appioasys.response

import com.google.gson.annotations.SerializedName

data class CompanyServiceResponse(
    @SerializedName("enterprise_type_name")
    val enterprise_type_name: String?
)
