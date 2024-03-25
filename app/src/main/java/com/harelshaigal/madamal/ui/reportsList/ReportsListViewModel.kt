package com.harelshaigal.madamal.ui.reportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harelshaigal.madamal.data.AppLocalDb
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.reportsList


// TODO this is the right place
class ReportsListViewModel : ViewModel() {
//    private val _reportListData = MutableLiveData<List<Report>>().apply {
//        value = reportsList()
//    }
//
//    val reportListData: LiveData<List<Report>> = _reportListData

    private val reportDao by lazy { AppLocalDb.db.reportDao()!! }

    val reports: LiveData<List<Report>> = reportDao.getAllReports()
}