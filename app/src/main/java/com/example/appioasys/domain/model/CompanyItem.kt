package com.example.appioasys.domain.model

import java.io.Serializable

class CompanyItem(
    val name: String?,
    val photoUrl: String?,
    val description: String?,
    val city: String?,
    val serviceType: String?
) : Serializable