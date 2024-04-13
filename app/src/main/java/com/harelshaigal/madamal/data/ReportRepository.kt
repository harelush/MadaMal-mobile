package com.harelshaigal.madamal.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportRepository() {
    private val db = FirebaseFirestore.getInstance()
    private val reportDao by lazy { AppLocalDb.db.reportDao()!! }
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    fun getAllReports(userId: String? = null): LiveData<List<Report>> {
        fetchLatestReportsFromFirebase(userId)
        return if (userId == null) {
            // No userId specified, return all reports
            reportDao.getAllReports()
        } else {
            // userId specified, return reports for the user
            reportDao.getReportsByUserId(userId)
        }
    }


    private fun fetchLatestReportsFromFirebase(userId: String? = null) {
        repositoryScope.launch {
            val latestTimestamp = reportDao.getLatestTimestamp() ?: 0
            var query = db.collection("reports")
                .whereGreaterThan("lastUpdated", latestTimestamp)

            // If a userId is provided, further restrict the query to fetch only the user's reports
            userId?.let {
                query = query.whereEqualTo("userId", it)
            }

            query.get()
                .addOnSuccessListener { documents ->
                    handleFirebaseSuccess(documents)
                }
                .addOnFailureListener { exception ->
                    Log.d("ReportRepository", "Error getting documents: ", exception)
                }
        }
    }


    private fun List<ReportDto>.toReportEntities(): List<Report> {
        return this.map { dto ->
            Report(
                userId = dto.userId,
                title = dto.title,
                data = dto.data,
                lat = dto.lat,
                lng = dto.lng,
                image = dto.image,
                lastUpdated = dto.lastUpdated
            )
        }
    }

    private fun handleFirebaseSuccess(documents: QuerySnapshot) {
        if (documents.isEmpty) return
        val reportDtos = documents.toObjects(ReportDto::class.java) // Fetching DTOs
        val reports = reportDtos.toReportEntities() // Mapping DTOs to Entities
        repositoryScope.launch {
            insertReports(reports)
        }
    }

    private suspend fun insertReports(reports: List<Report>) {
        withContext(Dispatchers.IO) {
            reports.forEach { report ->
                // Insert each report individually to get its new ID
                val id = reportDao.insertReport(report)
                // Update report with the new ID
                val updatedReport = report.copy(id = id)
                saveReportToFirebase(updatedReport)
            }
        }
    }

    suspend fun insertReport(report: Report) {
        withContext(Dispatchers.IO) {
            val reportWithUpdateTime = report.copy(
                lastUpdated = System.currentTimeMillis(),
            )

            val id = reportDao.insertReport(report)
            val updatedReport = reportWithUpdateTime.copy(id = id)
            saveReportToFirebase(updatedReport)
        }
    }


    private fun saveReportToFirebase(report: Report) {
        db.collection("reports")
            .document(report.id.toString())
            .set(report)
            .addOnSuccessListener {
                Log.d("ReportRepository", "Report saved with ID: ${report.id}")
            }
            .addOnFailureListener { e ->
                Log.w("ReportRepository", "Error adding report", e)
            }
    }

    fun getReportById(id: Long): LiveData<Report> {
        return reportDao.getReportById(id)
    }

    fun getReportsByUserId(userId: String): LiveData<List<Report>> {
        return reportDao.getReportsByUserId(userId)
    }

    fun deleteReportById(id: Long) {
        repositoryScope.launch {
            reportDao.deleteReportById(id)
            deleteReportFromFirebase(id)
        }
    }

    suspend fun updateReport(report: Report) {
        withContext(Dispatchers.IO) {
            reportDao.updateReport(report)
            saveReportToFirebase(report) // Assuming you want to update the report in Firebase as well
        }
    }

    private fun deleteReportFromFirebase(id: Long) {
        db.collection("reports")
            .document(id.toString())
            .delete()
            .addOnSuccessListener {
                Log.d("ReportRepository", "Report successfully deleted from Firebase with ID: $id")
            }
            .addOnFailureListener { e ->
                Log.w("ReportRepository", "Error deleting report from Firebase", e)
            }
    }
}
