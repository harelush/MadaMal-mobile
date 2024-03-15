package com.harelshaigal.madamal.ui.reportsList

import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding
import com.harelshaigal.madamal.ui.addReport.AddReportActivity
import com.harelshaigal.madamal.ui.reportsList.utils.DeleteDialogUtils

class ReportViewHolder(private val context: Context,
                       binding: FragmentReportListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val ownerIdView: TextView = binding.reportOwnerId
    private val creationDateView: TextView = binding.reportCreationDate
    private val dataView: TextView = binding.reportData
    private var currentReport : Report? = null

    init {
        binding.deleteReport.setOnClickListener {
            DeleteDialogUtils.createDeleteDialog(context) {
                // Perform delete operation here
            }
        }

        binding.editReport.setOnClickListener {
            val intent = Intent(context, AddReportActivity::class.java).apply {
                putExtra("reportId", currentReport?.id)
                putExtra("content", currentReport?.data)
                putExtra("imageURL", currentReport?.image)
            }
            context.startActivity(intent)
        }
    }
    fun bind(report: Report) {
        currentReport = report
        ownerIdView.text = report.ownerId.toString()
        dataView.text = report.data

    }
}