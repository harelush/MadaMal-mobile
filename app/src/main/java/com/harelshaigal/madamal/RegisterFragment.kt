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
import com.harelshaigal.madamal.databinding.FragmentRegisterBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    val auth = Firebase.auth
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
            .addErrorCallback {
                binding.registerPasswordEditText.error = it
            }
            .check()

        if (isEmailValid && isPasswordValid) {
            auth.createUserWithEmailAndPassword(
                binding.registerEmailEditText.text.toString().trim(),
                binding.registerPasswordEditText.text.toString().trim(),
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, "Error sign up, try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
