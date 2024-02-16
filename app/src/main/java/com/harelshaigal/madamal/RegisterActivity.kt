package com.harelshaigal.madamal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.harelshaigal.madamal.databinding.ActivityRegisterBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerRegisterButton.setOnClickListener {
            validateEmailAndPassword()
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
            .atleastOneUpperCase()
            .addErrorCallback {
                binding.registerPasswordEditText.error = it
            }
            .check()


        if (isEmailValid && isPasswordValid) {
            // Both fields are valid, proceed with the next steps
        }
    }
}