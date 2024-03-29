package com.harelshaigal.madamal.ui.reportsList

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment
import com.harelshaigal.madamal.ui.reportDialogs.DeleteReportDialog
import com.squareup.picasso.Picasso

class ReportViewHolder(
    private val context: Context,
    binding: FragmentReportListItemBinding,
    fragmentManager: FragmentManager
) :
    RecyclerView.ViewHolder(binding.root) {
    private val ownerIdView: TextView = binding.reportOwnerId
//    private val creationDateView: TextView = binding.reportCreationDate
    private val dataView: TextView = binding.reportData
    private val imgView: ImageView = binding.reportImage
    private var currentReport: Report? = null

    init {
        binding.deleteReport.setOnClickListener {
            DeleteReportDialog.createDeleteDialog(context) {
                // Perform delete operation here
            }
        }

        binding.editReport.setOnClickListener {
            ReportDialogFormFragment.display(fragmentManager, currentReport)
        }
    }

    fun bind(report: Report) {
        currentReport = report
        ownerIdView.text = report.ownerId.toString()
        dataView.text = report.data
        if (report.image != null) {
            Picasso.get().load(Uri.parse(report.image)).into(imgView)
        }

    }
}