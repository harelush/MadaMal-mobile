package com.harelshaigal.madamal.ui.reportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.reportsList

class ReportsListViewModel : ViewModel() {
    private val _reportListData = MutableLiveData<List<Report>>().apply {
        value = reportsList()
    }

    val reportListData: LiveData<List<Report>> = _reportListData
}