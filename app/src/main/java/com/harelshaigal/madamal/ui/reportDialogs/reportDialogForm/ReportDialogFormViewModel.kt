package com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm

import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.report.ReportRepository

class ReportDialogFormViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()

    fun getReportData(reportId: Long) = repository.getReportById(reportId)
}