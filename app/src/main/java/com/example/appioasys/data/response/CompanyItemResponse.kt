package com.example.appioasys.data.response
import com.google.gson.annotations.SerializedName

data class CompanyItemResponse(
    @SerializedName("enterprise_name")
    val companyName: String?,
    @SerializedName("photo")
    val photoUrl: String?,
    @SerializedName("description")
    val companyDescription: String?,
    @SerializedName("city")
    val companyCity: String?,
    @SerializedName("enterprise_type")
    val kindOfService: CompanyServiceResponse
)