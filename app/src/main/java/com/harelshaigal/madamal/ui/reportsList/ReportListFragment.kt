package com.harelshaigal.madamal.ui.reportsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.harelshaigal.madamal.databinding.FragmentReportsListBinding
import com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm.ReportDialogFormFragment

class ReportListFragment : Fragment() {
    companion object {
        private const val PARAM_KEY = "userId"

        fun newInstance(userId: String?): ReportListFragment {
            val fragment = ReportListFragment()
            val args = Bundle()
            args.putString(PARAM_KEY, userId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var reportAdapter: ReportListIAdapter
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

        reportAdapter = ReportListIAdapter(getParentFragmentManager())
        _binding = FragmentReportsListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView: RecyclerView = binding.reportList
        recyclerView.adapter = reportAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getReportList(arguments?.getString(PARAM_KEY)).observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.reportList.visibility= View.GONE
                binding.noReportText.visibility= View.VISIBLE
            } else {
                binding.reportList.visibility= View.VISIBLE
                binding.noReportText.visibility= View.GONE
            }

            reportAdapter.submitList(it.toMutableList())
        }

        binding.addReportButton.setOnClickListener {
            ReportDialogFormFragment.display(getParentFragmentManager(), null)
        }
    }
}