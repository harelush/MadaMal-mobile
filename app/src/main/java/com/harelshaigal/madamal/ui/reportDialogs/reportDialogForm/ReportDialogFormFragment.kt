package com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.R
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.ReportRepository
import com.harelshaigal.madamal.databinding.FragmentReportDialogFormBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ReportDialogFormFragment : DialogFragment(), ImagePickerHelper.ImagePickerCallback {

    private lateinit var imagePickerHelper: ImagePickerHelper
    val TAG = "add_or_edit_report_dialog"
    private lateinit var viewModel: ReportDialogFormViewModel
    private var _binding: FragmentReportDialogFormBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null
    private lateinit var mMap: GoogleMap
    private var selectedLocation: LatLng? = null
    private val repostRepository: ReportRepository = ReportRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ReportDialogFormViewModel::class.java]

        _binding = FragmentReportDialogFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imagePickerHelper = ImagePickerHelper(this, this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.addReportImageView.setOnClickListener {
            imagePickerHelper.openImagePicker()
        }

        binding.saveButton.setOnClickListener {
            runBlocking {
                launch {
                    saveReport()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            setEditData()
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEditData() {
        val content = arguments?.getString("content")
        val imageURL = arguments?.getString("imageURL")

        if (content != null || imageURL != null) {
            binding.reportDialogTitle.text = getString(R.string.title_edit_report_dialog)
            if (content != null) {
                binding.addReportContent.setText(content)
            }

            if (imageURL != null) {
                Picasso.get().load(Uri.parse(imageURL)).into(binding.addReportImageView)
            }
        }
    }

    companion object {
        fun display(fragmentManager: FragmentManager?, reportToEdit: Report? = null) {
            if (fragmentManager != null) {
                val reportDialogFormFragment = ReportDialogFormFragment()

                if (reportToEdit != null) {
                    val args = Bundle()
                    args.putString("content", reportToEdit.data)
                    args.putString("imageURL", reportToEdit.image)

                    reportDialogFormFragment.arguments = args
                }

                reportDialogFormFragment.show(
                    fragmentManager, reportDialogFormFragment.TAG
                )
            }
        }
    }

    override fun getImageViewForLoad(): ImageView {
        return binding.addReportImageView
    }

    override fun selectedImageExtraLogic(uri: Uri?) {
        uri.also { selectedImageUri = it }
    }

    private suspend fun saveReport() {
        val user = Firebase.auth.currentUser

        if (user != null) {
            val fileName = "reportsImages/${user.uid}/reportImage.jpg"

            try {
                val downloadUri: Uri? = ImagePickerHelper.uploadImageToFirebaseStorage(
                    selectedImageUri, fileName, context
                )

                // TODO - gal add here location:
                val lat = 37.4220
                val lng = 31.0841


                repostRepository.insertReport(
                    Report(
                        userId = user.uid,
                        data = binding.addReportContent.text.toString(),
                        lat = lat,
                        lng = lng,
                        image = downloadUri.toString(),
                    )
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Upload failed: ${e.message}")
                }
            }
        }

    }
}