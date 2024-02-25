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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.harelshaigal.madamal.databinding.FragmentRegisterBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val auth = Firebase.auth
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding.registerProfileImageView.setImageURI(uri)
                    selectedImageUri = uri
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
            showProgressBar(true)
            CoroutineScope(Dispatchers.IO).launch {
                registerUser()
            }
        }

        binding.registerSignInLink.setOnClickListener {
            (activity as? LoginActivity)?.replaceFragment(LoginFragment())
        }

        binding.registerProfileImageView.setOnClickListener {
            openGalleryForImage()
        }
    }

    private suspend fun registerUser() {
        if (validateEmailAndPassword()) {
            val email = binding.registerEmailEditText.text.toString().trim()
            val password = binding.registerPasswordEditText.text.toString().trim()

            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                uploadImageToFirebaseStorage()?.let {
                    withContext(Dispatchers.Main) {
                        navigateToMainActivity()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Registration failed: ${e.message}")
                }
            } finally {
                withContext(Dispatchers.Main) {
                    showProgressBar(false)
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                showProgressBar(false)
                showToast("Invalid email or password")
            }
        }
    }

    private fun validateEmailAndPassword(): Boolean {
        val isEmailValid = binding.registerEmailEditText.validator().nonEmpty().validEmail().check()
        val isPasswordValid =
            binding.registerPasswordEditText.validator().nonEmpty().atleastOneNumber().check()
        return isEmailValid && isPasswordValid
    }

    private suspend fun uploadImageToFirebaseStorage(): Uri? {
        return selectedImageUri?.let { uri ->
            val ref =
                Firebase.storage.reference.child("images/${Firebase.auth.currentUser?.uid}/profile.jpg")
            ref.putFile(uri).await()
            ref.downloadUrl.await()
        }
    }

    private fun openGalleryForImage() {
        imagePickerLauncher.launch("image/*")
    }

    private fun showProgressBar(show: Boolean) {
        binding.registerProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
