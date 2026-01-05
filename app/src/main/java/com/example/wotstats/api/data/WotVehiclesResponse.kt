package com.example.wotstats.api.data

data class WotVehiclesResponse(
    val status: String,
    val meta: Meta,
    val data: Map<String, Vehicle>
)
