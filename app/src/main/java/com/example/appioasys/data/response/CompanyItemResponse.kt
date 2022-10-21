package com.example.appioasys.data.response
import com.google.gson.annotations.SerializedName

data class CompanyItemResponse(
    @SerializedName("enterprise_name")
    val name: String?,
    @SerializedName("photo")
    val photoUrl: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("enterprise_type")
    val companyServiceResponse: CompanyServiceResponse?
)