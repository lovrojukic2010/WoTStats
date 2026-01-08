package com.example.wotstats.api.data

import com.google.gson.annotations.SerializedName

data class VehicleDetails(
    val hp: Int?,
    val weight: Int?,
    @SerializedName("speed_forward")
    val speedForward: Int?,
    val ammo: List<AmmoDetails>?,
    val engine: EngineDetails?,
    val gun: GunDetails?
)
