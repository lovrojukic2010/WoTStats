package com.example.wotstats.api.client

import com.example.wotstats.api.data.WotVehiclesResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WotClient(
    private val applicationId: String,
    private val api: WotApi
) {

    suspend fun getVehicles(
        limit: Int,
        pageNo: Int,
        tier: Int? = null,
        nation: String? = null
    ): WotVehiclesResponse {
        return api.getVehicles(
            appId = applicationId,
            limit = limit,
            pageNo = pageNo,
            tier = tier,
            nation = nation,
        )
    }

    companion object {
        fun create(applicationId: String): WotClient {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.worldoftanks.eu")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(WotApi::class.java)
            return WotClient(applicationId, api)
        }
    }

    object WotService {
        private const val APPLICATION_ID = "c7e9797137a92004d350eb3d56c5d0bd"

        val client: WotClient by lazy {
            create(APPLICATION_ID)
        }
    }
}