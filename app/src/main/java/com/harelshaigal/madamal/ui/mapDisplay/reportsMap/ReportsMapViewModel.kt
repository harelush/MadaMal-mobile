package com.harelshaigal.madamal.ui.mapDisplay.reportsMap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportsMapViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is report map Fragment"
    }
    val text: LiveData<String> = _text}