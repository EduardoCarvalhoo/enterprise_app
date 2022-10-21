package com.example.appioasys.data.response

import com.google.gson.annotations.SerializedName

data class CompanyServiceResponse(
    @SerializedName("enterprise_type_name")
    val serviceType: String?
)
