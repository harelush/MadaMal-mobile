package com.harelshaigal.madamal.ui.mapDisplay.reportMapDisplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportMapDisplayBinding
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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