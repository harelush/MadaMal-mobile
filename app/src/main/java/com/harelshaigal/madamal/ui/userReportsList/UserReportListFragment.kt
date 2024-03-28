package com.harelshaigal.madamal.ui.userReportsList

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

class UserReportListFragment : Fragment() {

    private lateinit var viewModel: UserReportsListViewModel

    private var _binding: FragmentReportsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[UserReportsListViewModel::class.java]

        _binding = FragmentReportsListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val reportAdapter = UserReportListIAdapter(getParentFragmentManager())

        val recyclerView: RecyclerView = binding.reportList
        recyclerView.adapter = reportAdapter

        viewModel.reports.observe(viewLifecycleOwner) {
            reportAdapter.submitList(it as MutableList<Report>)
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