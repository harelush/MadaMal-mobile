package com.harelshaigal.madamal.ui.mapDisplay.reportMapDisplay

import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.ReportRepository

class ReportMapDisplayViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()

    fun getReportData(reportId: Long) = repository.getReportById(reportId)
}