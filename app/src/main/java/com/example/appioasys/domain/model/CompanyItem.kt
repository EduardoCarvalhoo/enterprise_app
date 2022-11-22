package com.example.appioasys.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyItem(
    val name: String?,
    val photoUrl: String?,
    val description: String?,
    val city: String?,
    val serviceType: String?
): Parcelable