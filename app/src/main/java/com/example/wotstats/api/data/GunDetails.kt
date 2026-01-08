package com.example.wotstats.api.data

import com.google.gson.annotations.SerializedName

data class GunDetails(
    val dispersion: Float?,
    val caliber: Int?,
    @SerializedName("reload_time")
    val reloadTime: Float?,
    @SerializedName("fire_rate")
    val fireRate: Float?
)
