package com.example.wotstats.api.data

import com.google.gson.annotations.SerializedName

data class VehicleImages(
    @SerializedName("big_icon")
    val bigIcon: String?,
    @SerializedName("contour_icon")
    val contourIcon: String?,
    @SerializedName("small_icon")
    val smallIcon: String?
)
