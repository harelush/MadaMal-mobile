package com.harelshaigal.madamal.data.report

data class ReportDto (
    val id: Long = 0L,
    val userId: String = "",
    val title: String = "",
    val data: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val image: String? = null,
    val lastUpdated: Long? = null
)
