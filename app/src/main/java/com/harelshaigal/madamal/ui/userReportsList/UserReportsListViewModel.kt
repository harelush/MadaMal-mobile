package com.harelshaigal.madamal.ui.userReportsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserReportsListViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is user report list Fragment"
    }
    val text: LiveData<String> = _text
}