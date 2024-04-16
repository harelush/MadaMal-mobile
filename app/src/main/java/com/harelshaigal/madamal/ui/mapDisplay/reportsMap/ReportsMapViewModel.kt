package com.harelshaigal.madamal.ui.mapDisplay.reportsMap

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.ReportRepository

class ReportsMapViewModel : ViewModel() {
    private val repository: ReportRepository = ReportRepository()

    val reportList: LiveData<List<Report>> =  repository.getAllReports()
}