package com.harelshaigal.madamal.ui.userDispaly.userProfile

import OperationStatus
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harelshaigal.madamal.databinding.FragmentUserProfileBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.squareup.picasso.Picasso
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment(), ImagePickerHelper.ImagePickerCallback {
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var imagePickerHelper: ImagePickerHelper
    private var selectedImageUri: Uri? = null
    private var _binding: FragmentUserProfileBinding? = null
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

        binding.ImageButton.setOnClickListener {
            imagePickerHelper.openImagePicker()
        }

        viewModel.fetchUserData()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.userProfileEmailText.setText(user?.email ?: "")
            binding.userProfileFullNameText.setText(user?.fullName ?: "")
            if (user?.imageUri != null) {
                Picasso.get().load(user.imageUri).into(binding.userProfileProfileImageView)
                binding.userProfileProfileImageView.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editUserButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                updateUser()
            }
        }

        viewModel.updateStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                OperationStatus.LOADING -> {
                    binding.registerProgressBar.visibility = View.VISIBLE
                }

                OperationStatus.SUCCESS -> {
                    binding.registerProgressBar.visibility = View.GONE
                    Toast.makeText(context, "שינוי הנתונים נשמר בהצלחה", Toast.LENGTH_SHORT)
                        .show()
                }

                OperationStatus.FAILURE -> {
                    binding.registerProgressBar.visibility =
                        View.GONE
                    Toast.makeText(
                        context,
                        "שגיאה בשמירת הנתונים, נא לנסות שוב",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private suspend fun updateUser() {
        val newName = binding.userProfileFullNameText.text.toString()
        if (newName.nonEmpty()) {
            viewModel.updateUserDetails(newName, selectedImageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getImageViewForLoad(): ImageView = binding.userProfileProfileImageView

    override fun selectedImageExtraLogic(uri: Uri?) {
        uri.also {
            selectedImageUri = it
            binding.userProfileProfileImageView.visibility = View.VISIBLE
        }
    }
}
