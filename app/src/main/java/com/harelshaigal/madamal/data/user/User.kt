package com.harelshaigal.madamal.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey
    val uid: String,
    val fullName: String? = null,
    val email: String? = null,
    var imageUri: String? = null
)
