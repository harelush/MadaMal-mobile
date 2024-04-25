package com.harelshaigal.madamal.ui.mapDisplay.reportMapDisplay

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.harelshaigal.madamal.data.report.Report
import com.harelshaigal.madamal.databinding.FragmentReportMapDisplayBinding
import com.harelshaigal.madamal.helpers.Utils
import com.harelshaigal.madamal.ui.reportDialogs.DeleteReportDialog
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment
import com.squareup.picasso.Picasso

class ReportMapDisplayFragment : BottomSheetDialogFragment() {

    val TAG = "display_report_from_map"

    private lateinit var viewModel: ReportMapDisplayViewModel

    private var _binding: FragmentReportMapDisplayBinding? = null

    private val binding get() = _binding!!

    private var currentReportId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[ReportMapDisplayViewModel::class.java]

        _binding = FragmentReportMapDisplayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireArguments().getString("reportId")?.let {
            currentReportId = it
            viewModel.getReportData(it).observe(viewLifecycleOwner) { report ->
                if (report === null)
                    dismiss()
                else
                    setDisplayData(report)
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteReport.setOnClickListener {
            currentReportId?.let { it1 ->
                DeleteReportDialog.createDeleteDialog(view.context, it1)
            }
        }

        binding.editReport.setOnClickListener {
            ReportDialogFormFragment.display(parentFragmentManager, currentReportId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDisplayData(currentReport: Report) {
        binding.reportData.text = currentReport.data
        binding.reportTitle.text = currentReport.title
        binding.reportLastUpdateDate.text =
            currentReport.lastUpdated?.let { Utils.formatTimestampToString(it) }

        if (currentReport.image != "null" && currentReport.image != null) {
            Picasso.get().load(Uri.parse(currentReport.image)).into(binding.reportImage)
            binding.reportImage.visibility = View.VISIBLE
        }

        if (Firebase.auth.currentUser?.uid != currentReport.userId) {
            binding.actionButtonsContainer.visibility = View.GONE
        }
    }

    companion object {
        fun display(fragmentManager: FragmentManager?, reportId: String) {
            if (fragmentManager != null) {
                val reportMapDisplayFragment = ReportMapDisplayFragment()

                val args = Bundle()
                args.putString("reportId", reportId)

                reportMapDisplayFragment.arguments = args

                reportMapDisplayFragment.show(
                    fragmentManager,
                    reportMapDisplayFragment.TAG
                )
            }
        }
    }
}