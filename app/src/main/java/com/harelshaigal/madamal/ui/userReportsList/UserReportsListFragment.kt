package com.harelshaigal.madamal.ui.userReportsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentUserReportsListBinding

class UserReportsListFragment : Fragment() {

    private lateinit var viewModel: UserReportsListViewModel

    private var _binding: FragmentUserReportsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[UserReportsListViewModel::class.java]

        _binding = FragmentUserReportsListBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        viewModel..observe(viewLifecycleOwner) { reports ->
//            updateUI(reports)
//        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}