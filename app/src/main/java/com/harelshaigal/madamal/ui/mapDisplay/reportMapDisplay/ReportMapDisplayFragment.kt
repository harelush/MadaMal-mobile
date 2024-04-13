package com.harelshaigal.madamal.ui.mapDisplay.reportMapDisplay

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportMapDisplayBinding
import com.harelshaigal.madamal.ui.reportDialogs.DeleteReportDialog
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment
import com.squareup.picasso.Picasso

class ReportMapDisplayFragment : BottomSheetDialogFragment() {

    val TAG = "display_report_from_map"

    private lateinit var viewModel: ReportMapDisplayViewModel

    private var _binding: FragmentReportMapDisplayBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[ReportMapDisplayViewModel::class.java]

        _binding = FragmentReportMapDisplayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.deleteReport.setOnClickListener {
//            DeleteReportDialog.createDeleteDialog(view.context) {
//                // Perform delete operation here
//            }
//        }

        binding.editReport.setOnClickListener {
            ReportDialogFormFragment.display(parentFragmentManager)
        }

        setDisplayData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDisplayData() {
        val content = arguments?.getString("content")
        val imageURL = arguments?.getString("imageURL")

        if (content != null) {
            binding.reportData.text = content
        }

        if (imageURL != null) {
            Picasso.get().load(Uri.parse(imageURL)).into(binding.reportImage)
        }

    }

    companion object {
        fun display(fragmentManager: FragmentManager?, reportToDisplay: Report? = null) {
            if (fragmentManager != null) {
                val reportMapDisplayFragment = ReportMapDisplayFragment()

                if (reportToDisplay != null) {
                    val args = Bundle()
                    args.putString("content", reportToDisplay.data)
                    args.putString("imageURL", reportToDisplay.image)

                    reportMapDisplayFragment.arguments = args
                }

                reportMapDisplayFragment.show(
                    fragmentManager,
                    reportMapDisplayFragment.TAG
                )
            }
        }
    }
}