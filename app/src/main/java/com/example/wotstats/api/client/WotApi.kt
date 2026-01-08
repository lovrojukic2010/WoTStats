package com.example.wotstats.api.client

import com.example.wotstats.api.data.WotVehiclesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WotApi {

    @GET("/wot/encyclopedia/vehicles/")
    suspend fun getVehicles(
        @Query("application_id") appId: String,
        @Query("limit") limit: Int,
        @Query("tank_id") tankId: List<Int> = emptyList(),
        @Query("page_no") pageNo: Int,
        @Query("tier") tier: Int? = null,
        @Query("nation") nation: String? = null,
        @Query("fields") fields: String
    ): WotVehiclesResponse
}