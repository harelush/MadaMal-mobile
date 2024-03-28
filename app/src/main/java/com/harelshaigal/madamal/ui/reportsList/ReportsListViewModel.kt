package com.harelshaigal.madamal.ui.reportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.ReportRepository

class ReportsListViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()

    val reports: LiveData<List<Report>> = repository.getAllReports()
}