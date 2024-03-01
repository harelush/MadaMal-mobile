package com.harelshaigal.madamal.ui.reportslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportsListViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is report list Fragment"
    }
    val text: LiveData<String> = _text
}