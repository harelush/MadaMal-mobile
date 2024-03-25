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

    fun getAllReports(): LiveData<List<Report>> {
        fetchLatestReportsFromFirebase()
        return reportDao.getAllReports()
    }

    private fun fetchLatestReportsFromFirebase() {
        repositoryScope.launch {
            val latestTimestamp = reportDao.getLatestTimestamp() ?: 0
            db.collection("reports")
                .whereGreaterThan("lastUpdated", latestTimestamp)
                .get()
                .addOnSuccessListener { documents ->
                    handleFirebaseSuccess(documents)
                }
                .addOnFailureListener { exception ->
                    Log.d("ReportRepository", "Error getting documents: ", exception)
                }
        }
    }

    private fun handleFirebaseSuccess(documents: QuerySnapshot) {
        if (documents.isEmpty) return
        val reports = documents.toObjects(Report::class.java)
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
            val id = reportDao.insertReport(report)
            val updatedReport = report.copy(id = id)
            saveReportToFirebase(updatedReport)
        }
    }

    private fun saveReportToFirebase(report: Report) {
        val reportData = report.toMap()
        db.collection("reports")
            .document(report.id.toString())
            .set(reportData)
            .addOnSuccessListener {
                Log.d("ReportRepository", "Report saved with ID: ${report.id}")
            }
            .addOnFailureListener { e ->
                Log.w("ReportRepository", "Error adding report", e)
            }
    }
}
