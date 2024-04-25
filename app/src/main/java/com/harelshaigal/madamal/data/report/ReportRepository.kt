package com.harelshaigal.madamal.data.report

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.harelshaigal.madamal.data.AppLocalDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReportRepository() {
    private val reportsCollection = FirebaseFirestore.getInstance().collection("reports")
    private val reportDao by lazy { AppLocalDb.db.reportDao()!! }
    private lateinit var reportsRegistration: ListenerRegistration

    fun getAllReports(userId: String? = null): LiveData<List<Report>> {
        return if (userId == null) reportDao.getAllReports() else reportDao.getReportsByUserId(
            userId
        )
    }

    fun startReportsFetching() {
        reportsRegistration =
            reportsCollection.addSnapshotListener { snapshots: QuerySnapshot?, _: FirebaseFirestoreException? ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (snapshots?.isEmpty == false) {
                        for (dc in snapshots.documentChanges) {
                            when (dc.type) {
                                DocumentChange.Type.ADDED ->
                                    reportDao.insertReport(convertQueryTOReport(dc.document))

                                DocumentChange.Type.MODIFIED -> reportDao.updateReport(
                                    convertQueryTOReport(dc.document)
                                )

                                DocumentChange.Type.REMOVED -> reportDao.deleteReportById(dc.document.id)
                            }
                        }
                    }
                }
            }
    }

    fun endReportsFetching() = reportsRegistration.remove()

    private fun convertQueryTOReport(document: QueryDocumentSnapshot): Report {
        val reportDto = document.toObject(ReportDto::class.java) // Fetching DTOs
        return reportDto.toReport(document.id)
    }

    fun getReportById(id: String): LiveData<Report> {
        return reportDao.getReportById(id)
    }

     fun updateReport(reportDto: ReportDto, reportId: String) = reportsCollection.document(reportId).set(reportDto.toMap())

     fun addReport(reportDto: ReportDto) = reportsCollection.add(reportDto.toMap())

    fun deleteReportById(id: String) = reportsCollection.document(id).delete()
}
