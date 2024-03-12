package com.harelshaigal.madamal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ReportDto {
    @get:Query("select * from Report")
    val all: List<Report?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg students: Report?)

    @Delete
    fun delete(student: Report?)
}