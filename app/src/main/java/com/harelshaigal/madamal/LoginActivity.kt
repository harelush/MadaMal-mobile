package com.harelshaigal.madamal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.harelshaigal.madamal.databinding.ActivityLoginBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLoginButton.setOnClickListener {
            validateEmailAndPassword()
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
            // Both fields are valid, proceed with the next steps
        }
    }
}