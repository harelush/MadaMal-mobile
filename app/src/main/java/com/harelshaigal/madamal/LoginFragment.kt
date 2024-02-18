package com.harelshaigal.madamal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harelshaigal.madamal.databinding.FragmentLoginBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLoginButton.setOnClickListener {
            validateEmailAndPassword()
        }

        binding.loginSignUpLink.setOnClickListener {
            // Navigate to RegisterFragment
            (activity as? LoginActivity)?.replaceFragment(RegisterFragment())
        }
    }

    private fun validateEmailAndPassword() {
        val isEmailValid = binding.loginEmailEditText.validator()
            .nonEmpty()
            .validEmail()
            .addErrorCallback {
                binding.loginEmailEditText.error = it
            }
            .check()

        val isPasswordValid = binding.loginPasswordEditText.validator()
            .nonEmpty()
            .atleastOneNumber()
            .atleastOneSpecialCharacters()
            .atleastOneUpperCase()
            .addErrorCallback {
                binding.loginPasswordEditText.error = it
            }
            .check()

        if (isEmailValid && isPasswordValid) {
            // Proceed with login logic or next steps
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding when the view is destroyed
    }
}
