package com.harelshaigal.madamal.ui.userProfile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.harelshaigal.madamal.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {

    private lateinit var viewModel: UserProfileViewModel

    private var _binding: FragmentUserProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[UserProfileViewModel::class.java]

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textUserProfile
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