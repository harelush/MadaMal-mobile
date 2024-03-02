package com.harelshaigal.madamal.ui.reportsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.reportsList
import com.harelshaigal.madamal.databinding.FragmentReportsListBinding

class ReportListFragment : Fragment() {

    private  val viewModel by viewModels<ReportsListViewModel> {
        ReportsListViewModelFactory(this)
    }
    private var _binding: FragmentReportsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val reportAdapter = ReportListIAdapter(reportsList())

        val recyclerView: RecyclerView = binding.reportList
        recyclerView.adapter = reportAdapter

        viewModel.reportListData.observe(viewLifecycleOwner) {
//            reportAdapter.(it as MutableList<Report>)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}