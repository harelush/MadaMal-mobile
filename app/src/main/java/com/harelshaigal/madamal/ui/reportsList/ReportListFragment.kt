package com.harelshaigal.madamal.ui.reportsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportsListBinding
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment

class ReportListFragment : Fragment() {

    private lateinit var viewModel: ReportsListViewModel

    private var _binding: FragmentReportsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[ReportsListViewModel::class.java]

        _binding = FragmentReportsListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val reportAdapter = ReportListIAdapter(getParentFragmentManager())

        val recyclerView: RecyclerView = binding.reportList
        recyclerView.adapter = reportAdapter

        viewModel.reports.observe(viewLifecycleOwner) {
            reportAdapter.submitList(it.toMutableList())
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addReportButton.setOnClickListener {
            ReportDialogFormFragment.display(getParentFragmentManager())
        }
    }
}