package com.harelshaigal.madamal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.harelshaigal.madamal.databinding.FragmentLoginBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class LoginFragment : Fragment() {
    val auth = Firebase.auth
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
            .addErrorCallback {
                binding.loginPasswordEditText.error = it
            }
            .check()

        if (isEmailValid && isPasswordValid) {
            auth.signInWithEmailAndPassword(
                binding.loginEmailEditText.text.toString().trim(),
                binding.loginPasswordEditText.text.toString().trim(),
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, "Error sign in, try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding when the view is destroyed
    }
}
