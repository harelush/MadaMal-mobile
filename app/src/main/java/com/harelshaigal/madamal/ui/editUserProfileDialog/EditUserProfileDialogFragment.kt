package com.harelshaigal.madamal.ui.editUserProfileDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.harelshaigal.madamal.databinding.FragmentEditUserProfileDialogBinding


class EditUserProfileDialogFragment : DialogFragment() {

    val TAG = "edit_user_profile_dialog"

    fun display(fragmentManager: FragmentManager?): EditUserProfileDialogFragment {
        val exampleDialog = EditUserProfileDialogFragment()
        if (fragmentManager != null) {
            exampleDialog.show(fragmentManager, TAG)
        }
        return exampleDialog
    }
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


        return root
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

}