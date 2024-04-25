package com.harelshaigal.madamal.data.report

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Report (
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val data: String,
    val lat: Double?,
    val lng: Double?,
    var image: String? = null,
    val lastUpdated: Long? = System.currentTimeMillis()
)


