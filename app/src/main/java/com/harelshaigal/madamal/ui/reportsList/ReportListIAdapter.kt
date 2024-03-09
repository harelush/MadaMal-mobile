package com.harelshaigal.madamal.ui.reportsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding

class ReportListIAdapter : ListAdapter<Report, ReportViewHolder>(ReportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = FragmentReportListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReportViewHolder(
            parent.context, binding
        )

    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport: Report = getItem(position)
        holder.bind(currentReport)

    }
}

