package com.harelshaigal.madamal.data.report

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReportDao {
    @Query("SELECT * FROM Report ORDER BY lastUpdated DESC")
    fun getAllReports(): LiveData<List<Report>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(report: Report): Long

    @Insert
    fun insertReports(reports: List<Report>): LongArray

    @Delete
    fun deleteReport(report: Report)

    @Query("SELECT MAX(lastUpdated) FROM Report")
    fun getLatestTimestamp(): Long?

    @Query("SELECT * FROM Report WHERE id = :id")
    fun getReportById(id: Long): LiveData<Report>

    @Query("SELECT * FROM Report WHERE userId = :userId ORDER BY lastUpdated DESC")
    fun getReportsByUserId(userId: String): LiveData<List<Report>>

    @Query("DELETE FROM Report WHERE id = :id")
    fun deleteReportById(id: Long)

    @Update
    fun updateReport(report: Report)
}
