package com.harelshaigal.madamal.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.MainActivity
import com.harelshaigal.madamal.databinding.FragmentLoginBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
            _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLoginButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                validateEmailAndPassword()
            }
        }

        binding.loginSignUpLink.setOnClickListener {
            (activity as? LoginActivity)?.replaceFragment(RegisterFragment())
        }
    }

    private suspend fun validateEmailAndPassword() {
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
            try {
                showProgressBar(true)
                auth.signInWithEmailAndPassword(
                    binding.loginEmailEditText.text.toString().trim(),
                    binding.loginPasswordEditText.text.toString().trim(),
                ).await()
                navigateToMainActivity()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error sign in, try again", Toast.LENGTH_SHORT).show()
                }
            } finally {
                showProgressBar(false)
            }
        }
    }

    private fun showProgressBar(show: Boolean) {
        binding.loginProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
