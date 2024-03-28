package com.harelshaigal.madamal.ui.userReportsList

import androidx.recyclerview.widget.DiffUtil
import com.harelshaigal.madamal.data.Report

class UserReportDiffCallback : DiffUtil.ItemCallback<Report>() {
    override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
        oldItem.id == newItem.id

}