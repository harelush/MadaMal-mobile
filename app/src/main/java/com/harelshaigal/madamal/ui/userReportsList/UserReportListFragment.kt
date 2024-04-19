package com.harelshaigal.madamal.ui.userReportsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.harelshaigal.madamal.databinding.FragmentUserReportsListBinding
import com.harelshaigal.madamal.ui.reportsList.ReportListFragment

class UserReportListFragment : Fragment() {
    private var _binding: FragmentUserReportsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserReportsListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reportsListFragment = ReportListFragment.newInstance(Firebase.auth.currentUser?.uid)
        childFragmentManager.beginTransaction()
            .replace(binding.reportsListFragmentContainer.id, reportsListFragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}