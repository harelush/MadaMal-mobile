package com.harelshaigal.madamal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Report (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val data: String,
    val lat: Double?,
    val lng: Double?,
//    @DrawableRes
    val image: String? = null,
    val lastUpdated: Long? = null
)

fun Report.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "userId" to userId,
        "data" to data,
        "lat" to lat,
        "lng" to lng,
        "image" to image,
        "lastUpdated" to lastUpdated
    )
}

