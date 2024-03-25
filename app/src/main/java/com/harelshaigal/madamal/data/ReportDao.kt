package com.harelshaigal.madamal.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReportDao {
    @Query("SELECT * FROM Report")
    fun getAllReports(): LiveData<List<Report>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(report: Report): Long

    @Insert
    fun insertReports(reports: List<Report>): LongArray

    @Delete
    fun deleteReport(report: Report)

    @Query("SELECT MAX(lastUpdated) FROM Report")
    fun getLatestTimestamp(): Long?
}
