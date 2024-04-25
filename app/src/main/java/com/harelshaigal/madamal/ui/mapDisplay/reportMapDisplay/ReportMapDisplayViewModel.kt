package com.harelshaigal.madamal.ui.mapDisplay.reportMapDisplay

import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.report.ReportRepository

class ReportMapDisplayViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()

    fun getReportData(reportId: String) = repository.getReportById(reportId)
}