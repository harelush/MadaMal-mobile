package com.harelshaigal.madamal.ui.userReportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.ReportRepository

class UserReportsListViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()
    val user = Firebase.auth.currentUser
    val reports: LiveData<List<Report>> = repository.getReportsByUserId(user?.uid!!).map { reports ->
        reports.sortedByDescending { report -> report.lastUpdated }
    }
}