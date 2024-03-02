package com.harelshaigal.madamal.ui.reportsList

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding

class ReportListIAdapter(
    private val values: List<Report>
) : RecyclerView.Adapter<ReportListIAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        return ReportViewHolder(
            FragmentReportListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport: Report = values[position]
       holder.bind(currentReport)
    }

    class ReportViewHolder(binding: FragmentReportListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ownerIdView: TextView = binding.reportOwnerId
        private val creationDateView: TextView = binding.reportCreationDate
        private val dataView: TextView = binding.reportData
        private var currentReport : Report? = null

        fun bind(report: Report) {
            currentReport = report
            ownerIdView.text = report.ownerId.toString()
            creationDateView.text = report.creationDate.toString()
            dataView.text = report.data

        }
    }
}

object ReportDiffCallback : DiffUtil.ItemCallback<Report>() {
    override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
        return oldItem.id == newItem.id
    }
}