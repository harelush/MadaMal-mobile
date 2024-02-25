package com.harelshaigal.madamal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.harelshaigal.madamal.databinding.FragmentRegisterBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    val auth = Firebase.auth
    private val binding get() = _binding!!

    // Initialize ActivityResultLauncher
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register the launcher and define the result handling logic
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Update the ImageView with the selected image URI
            uri?.let {
                binding.registerProfileImageView.setImageURI(uri)
            }
        }
    }

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

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.registerProfileImageView.setImageURI(uri)
            }
        }

        binding.registerProfileImageView.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        imagePickerLauncher.launch("image/*")
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
