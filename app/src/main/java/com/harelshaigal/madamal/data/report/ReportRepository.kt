package com.harelshaigal.madamal.data.report

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.harelshaigal.madamal.data.AppLocalDb
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.harelshaigal.madamal.helpers.Utils
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

    private suspend fun uploadImage(selectedImageUri: Uri, reportId: String): String =
        ImagePickerHelper.uploadImageToFirebaseStorage(
            selectedImageUri,
            Utils.getReportImageName(reportId)
        ).toString()


    suspend fun updateReport(reportDto: ReportDto, reportId: String, selectedImageUri: Uri?) {
        if (selectedImageUri !== null)
            reportDto.image = uploadImage(selectedImageUri, reportId)
        reportsCollection.document(reportId).set(reportDto.toMap())
    }

    suspend fun addReport(reportDto: ReportDto, selectedImageUri: Uri?) {
        val newReportRef: DocumentReference = reportsCollection.document()
        if (selectedImageUri !== null)
            reportDto.image = uploadImage(selectedImageUri, newReportRef.id)

        newReportRef.set(reportDto.toMap())
    }

    fun deleteReportById(id: String) = reportsCollection.document(id).delete()
}
