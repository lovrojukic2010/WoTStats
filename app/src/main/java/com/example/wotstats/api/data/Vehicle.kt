package com.example.wotstats.api.data

import com.google.gson.annotations.SerializedName

data class Vehicle(
    @SerializedName("tank_id")
    val tankId: Int,
    val name: String?,
    @SerializedName("short_name")
    val shortName: String?,
    val nation: String?,
    val tier: Int?,
    val images: VehicleImages?,
    @SerializedName("default_profile")
    val details: VehicleDetails?
)
