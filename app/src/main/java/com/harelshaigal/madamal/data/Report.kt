package com.harelshaigal.madamal.data

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

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
