package com.harelshaigal.madamal.ui.userDispaly.editUserProfileDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.harelshaigal.madamal.databinding.FragmentEditUserProfileDialogBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper


class EditUserProfileDialogFragment : DialogFragment(), ImagePickerHelper.ImagePickerCallback {

    private lateinit var imagePickerHelper: ImagePickerHelper

    val TAG = "edit_user_profile_dialog"

    private lateinit var viewModel: EditUserProfileDialogViewModel

    private var _binding: FragmentEditUserProfileDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[EditUserProfileDialogViewModel::class.java]

        _binding = FragmentEditUserProfileDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imagePickerHelper = ImagePickerHelper(this, this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.userProfileProfileImageView.setOnClickListener {
            imagePickerHelper.openImagePicker()
        }

        binding.editUserButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun display(fragmentManager: FragmentManager) {
            val editUserProfileDialogFragment = EditUserProfileDialogFragment()
            editUserProfileDialogFragment.show(
                fragmentManager,
                editUserProfileDialogFragment.TAG
            )
        }
    }

    override fun getImageViewForLoad(): ImageView {
        return binding.userProfileProfileImageView
    }

}