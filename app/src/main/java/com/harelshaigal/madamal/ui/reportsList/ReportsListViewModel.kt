package com.harelshaigal.madamal.ui.reportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.report.Report
import com.harelshaigal.madamal.data.report.ReportRepository

class ReportsListViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()

    fun getReportList(userId: String?): LiveData<List<Report>> = repository.getAllReports(userId)
}