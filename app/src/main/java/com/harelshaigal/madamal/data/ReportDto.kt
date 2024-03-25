package com.harelshaigal.madamal.data

data class ReportDto (
    val id: Long = 0L,
    val userId: String = "",
    val data: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val image: String? = null,
    val lastUpdated: Long? = null
)
