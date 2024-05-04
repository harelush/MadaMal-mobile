package com.harelshaigal.madamal.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.harelshaigal.madamal.helpers.LocationHelper

class MadamalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        LocationHelper.initialize(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

}