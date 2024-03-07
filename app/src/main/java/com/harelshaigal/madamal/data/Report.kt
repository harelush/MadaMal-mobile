package com.harelshaigal.madamal.data

import android.location.Location
import androidx.annotation.DrawableRes
import java.util.Date

data class Report (
    val id: Long,
    val ownerId: Long,
    val creationDate: Date,
    val data: String,
    val location: Location? = null,
    @DrawableRes
    val image: Int? = null,
)