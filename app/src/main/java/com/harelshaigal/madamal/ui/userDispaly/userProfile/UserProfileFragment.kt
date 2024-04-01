package com.harelshaigal.madamal.ui.userDispaly.userProfile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.harelshaigal.madamal.databinding.FragmentUserProfileBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.tasks.await

class UserProfileFragment : Fragment(), ImagePickerHelper.ImagePickerCallback {
    private lateinit var viewModel: UserProfileViewModel
    private var _binding: FragmentUserProfileBinding? = null

    private lateinit var imagePickerHelper: ImagePickerHelper

    private var selectedImageUri: Uri? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imagePickerHelper = ImagePickerHelper(this, this)

        binding.userProfileProfileImageView.setOnClickListener {
            imagePickerHelper.openImagePicker()
        }

        val user = Firebase.auth.currentUser
        val userUID = user?.uid

        // Observe the user LiveData from the ViewModel
        viewModel.user.observe(viewLifecycleOwner) { user ->
            // Update UI
            binding.userProfileEmailText.setText(user?.email ?: "")
            binding.userProfileFullNameText.setText(user?.fullName ?: "")
            binding.editUserButton
        }

        viewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl != null && imageUrl.isNotEmpty()) {
                Picasso.get().load(imageUrl).into(binding.userProfileProfileImageView)
            } else {
                // Can load here deafult image
            }
        }

        viewModel.fetchUserData(userUID.toString())
        viewModel.fetchUserImageUrl(userUID.toString())

        binding.editUserButton.setOnClickListener {
            selectedImageUri?.let { uri ->
                viewModel.uploadUserImage(uri)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getImageViewForLoad(): ImageView = binding.userProfileProfileImageView
}
