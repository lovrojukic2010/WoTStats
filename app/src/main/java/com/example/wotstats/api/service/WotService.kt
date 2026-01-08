package com.example.wotstats.api.service

import com.example.wotstats.api.client.WotClient
import com.example.wotstats.api.data.Vehicle

class WotService (
    private val client: WotClient
) {
    suspend fun loadVehicles(
        limit: Int,
        pageNo: Int,
        tier: Int? = null,
        nation: String? = null,
        tankId: List<Int> = emptyList(),
        fields: String
    ): List<Vehicle> {
        val response = client.getVehicles(
            limit = limit,
            pageNo = pageNo,
            tier = tier,
            nation = nation,
            tankId = tankId,
            fields = fields
        )
        return response.data.values.toList()
    }
}