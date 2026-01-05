package com.example.wotstats.api.data

import com.google.gson.annotations.SerializedName

data class Meta(
    val total: Int,
    val limit: Int,
    val count: Int,
    val page: Int,
    @SerializedName("page_total")
    val pageTotal: Int,
)
