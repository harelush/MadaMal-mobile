package com.harelshaigal.madamal.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.MainActivity
import com.harelshaigal.madamal.data.user.User
import com.harelshaigal.madamal.data.user.UserRepository
import com.harelshaigal.madamal.databinding.FragmentRegisterBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.harelshaigal.madamal.helpers.ToastHelper
import com.harelshaigal.madamal.helpers.Utils
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment(), ImagePickerHelper.ImagePickerCallback {

    private lateinit var imagePickerHelper: ImagePickerHelper
    private var _binding: FragmentRegisterBinding? = null
    private val auth = Firebase.auth
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private val userRepository: UserRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickerHelper = ImagePickerHelper(this, this)
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

        binding.ImageButton.setOnClickListener {
            imagePickerHelper.openImagePicker()
        }
    }

    private suspend fun registerUser() {
        if (validateEmailAndPassword()) {
            val email = binding.registerEmailEditText.text.toString().trim()
            val password = binding.registerPasswordEditText.text.toString().trim()
            val fullName = binding.registerFullNameEditText.text.toString().trim()

            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .await().user?.let { firebaseUser ->
                        val user = User(uid = firebaseUser.uid, fullName = fullName, email = email)
                        // Save the user info to Firestore
                        saveUserToFirestore(user)
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    ToastHelper.showToast("Registration failed: ${e.message}", context)
                    showProgressBar(false)
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                showProgressBar(false)
                ToastHelper.showToast("Invalid email or password", context)
            }
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        try {
            if (selectedImageUri != null) {
                user.imageUri = ImagePickerHelper.uploadImageToFirebaseStorage(
                    selectedImageUri, Utils.getUserImageName(user.uid)
                ).toString()
            }

            userRepository.insertUser(user)

            withContext(Dispatchers.Main) {
                navigateToMainActivity()
                showProgressBar(false)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                ToastHelper.showToast("Failed to save user data: ${e.message}", context)
                showProgressBar(false)
            }
        }
    }

    private fun validateEmailAndPassword(): Boolean {
        val isEmailValid = binding.registerEmailEditText.validator().nonEmpty().validEmail().check()
        val isPasswordValid =
            binding.registerPasswordEditText.validator().nonEmpty().atleastOneNumber().check()
        return isEmailValid && isPasswordValid
    }

    private fun showProgressBar(show: Boolean) {
        binding.registerProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getImageViewForLoad(): ImageView = binding.registerProfileImageView

    override fun selectedImageExtraLogic(uri: Uri?) {
        uri.also {
            selectedImageUri = it
            binding.registerProfileImageView.visibility = View.VISIBLE
        }
    }
}
