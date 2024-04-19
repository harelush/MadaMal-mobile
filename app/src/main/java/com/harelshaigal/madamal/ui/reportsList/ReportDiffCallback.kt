package com.harelshaigal.madamal.ui.reportsList

import androidx.recyclerview.widget.DiffUtil
import com.harelshaigal.madamal.data.report.Report

class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {
    override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
        oldItem.id == newItem.id

}