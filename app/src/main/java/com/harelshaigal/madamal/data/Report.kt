package com.harelshaigal.madamal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Report (
    @PrimaryKey
    val id: Long,
    val ownerId: Long,
    val data: String,
    val lat: Double?,
    val lng: Double?,
//    @DrawableRes
    val image: String? = null,
)
