package com.harelshaigal.madamal.ui.reportsList

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.harelshaigal.madamal.R
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding
import com.harelshaigal.madamal.ui.addReport.AddReportActivity

class ReportListIAdapter :  ListAdapter<Report, ReportListIAdapter.ReportViewHolder>(ReportDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        return ReportViewHolder(
            FragmentReportListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val editButton: Button = holder.itemView.findViewById(R.id.editReport)

        val currentReport: Report = getItem(position)
       holder.bind(currentReport)

        editButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AddReportActivity::class.java).apply {
                putExtra("reportId", currentReport.id)
                putExtra("content", currentReport.data)
                putExtra("imageURL", currentReport.image)
            }
            context.startActivity(intent)
        }
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