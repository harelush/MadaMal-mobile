package com.harelshaigal.madamal.ui.reportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.reportsList

class ReportsListViewModel : ViewModel() {
    private val _reportListData = MutableLiveData<List<Report>>().apply {
        value = reportsList()
    }

    val reportListData: LiveData<List<Report>> = _reportListData
}

class ReportsListViewModelFactory(val context: ReportListFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportsListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}