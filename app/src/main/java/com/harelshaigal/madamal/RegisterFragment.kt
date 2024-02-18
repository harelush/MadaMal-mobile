package com.harelshaigal.madamal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harelshaigal.madamal.databinding.FragmentRegisterBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerRegisterButton.setOnClickListener {
            validateEmailAndPassword()
        }

        binding.registerSignInLink.setOnClickListener {
            // Navigate back to LoginFragment
            (activity as? LoginActivity)?.replaceFragment(LoginFragment())
        }
    }

    private fun validateEmailAndPassword() {
        val isEmailValid = binding.registerEmailEditText.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
                binding.registerEmailEditText.error = it
            }
            .check()

        val isPasswordValid = binding.registerPasswordEditText.validator()
            .nonEmpty()
            .atleastOneNumber()
            .atleastOneSpecialCharacters()
            .atleastOneSpecialCharacters()
            .addErrorCallback {
                binding.registerPasswordEditText.error = it
            }
            .check()

        if (isEmailValid && isPasswordValid) {
            // Proceed with registration logic or next steps
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
