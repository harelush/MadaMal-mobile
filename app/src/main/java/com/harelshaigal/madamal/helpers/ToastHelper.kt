package com.harelshaigal.madamal.helpers

import android.content.Context
import android.widget.Toast

object ToastHelper {
    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}