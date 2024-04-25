package com.harelshaigal.madamal.ui.reportsList

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.harelshaigal.madamal.data.report.Report
import com.harelshaigal.madamal.databinding.FragmentReportListItemBinding
import com.harelshaigal.madamal.helpers.Utils
import com.harelshaigal.madamal.ui.reportDialogs.DeleteReportDialog
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment
import com.squareup.picasso.Picasso

class ReportViewHolder(
    private val context: Context,
    private val binding: FragmentReportListItemBinding,
    fragmentManager: FragmentManager
) :
    RecyclerView.ViewHolder(binding.root) {
    private var currentReportId: String? = null

    init {
        binding.deleteReport.setOnClickListener {
            currentReportId?.let { it1 -> DeleteReportDialog.createDeleteDialog(context, it1) }
        }

        binding.editReport.setOnClickListener {
            ReportDialogFormFragment.display(fragmentManager, currentReportId)
        }
    }

    fun bind(report: Report) {
        currentReportId = report.id
        binding.reportTitle.text = report.title
        binding.reportDate.text = report.lastUpdated?.let { Utils.formatTimestampToString(it) }
        binding.reportData.text = report.data

        if (Firebase.auth.currentUser?.uid != report.userId) {
            binding.actionButtonsContainer.visibility = View.GONE
        }

        if (report.image != "null" && report.image != null) {
            Picasso.get().load(Uri.parse(report.image)).into(binding.reportImage)
            binding.reportImage.visibility = View.VISIBLE
        }
    }
}