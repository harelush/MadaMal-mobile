package com.harelshaigal.madamal.ui.userDispaly.userProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is user data Fragment"
    }
    val text: LiveData<String> = _text}