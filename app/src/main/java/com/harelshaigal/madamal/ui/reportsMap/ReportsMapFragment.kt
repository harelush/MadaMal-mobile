package com.harelshaigal.madamal.ui.reportsMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harelshaigal.madamal.databinding.FragmentReportsMapBinding

class ReportsMapFragment : Fragment() {

    private lateinit var viewModel: ReportsMapViewModel

    private var _binding: FragmentReportsMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[ReportsMapViewModel::class.java]

        _binding = FragmentReportsMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textReportMap
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