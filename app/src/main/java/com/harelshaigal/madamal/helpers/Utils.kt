package com.harelshaigal.madamal.helpers

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
class Utils {
    companion object {
        fun formatTimestampToString(timestamp: Long): String {
            val date = Date(timestamp)
            val format = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
            return format.format(date)
        }

        fun getUserImageName(id: String) = "images/users/${id}/profile.jpg"
        fun getReportImageName(id: String) = "reportsImages/${id}/reportImage.jpg"
    }
}