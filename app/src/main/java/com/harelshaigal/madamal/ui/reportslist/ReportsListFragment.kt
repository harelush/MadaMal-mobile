package com.harelshaigal.madamal.ui.reportslist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.harelshaigal.madamal.databinding.FragmentReportsListBinding

class ReportsListFragment : Fragment() {

    private lateinit var viewModel: ReportsListViewModel

    private var _binding: FragmentReportsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val textView: TextView = binding.textReportList
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}