package com.example.appioasys.data.response

import com.google.gson.annotations.SerializedName

data class CompanyListResponse(
    @SerializedName("enterprises")
    val companies: List<HomeItemResponse>
)