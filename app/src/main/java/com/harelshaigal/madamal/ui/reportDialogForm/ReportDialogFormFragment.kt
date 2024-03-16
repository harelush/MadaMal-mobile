package com.harelshaigal.madamal.ui.reportDialogForm

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.data.Report
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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private lateinit var mMap: GoogleMap
    private var selectedLocation: LatLng? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[ReportDialogFormViewModel::class.java]

        _binding = FragmentReportDialogFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imagePickerHelper = ImagePickerHelper(this, this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        if (content != null) {
            binding.addReportContent.setText(content)
        }

        if (imageURL != null) {
            Picasso.get().load(Uri.parse(imageURL)).into(binding.addReportImageView)
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
                    fragmentManager,
                    reportDialogFormFragment.TAG
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
                    selectedImageUri,
                    fileName,
                    context
                )

                println("SHAY: uploaded image")
                val db = Firebase.firestore
                val reportData = hashMapOf(
                    "content" to binding.addReportContent.text.toString(),
                    "userID" to user.uid,
                    "latitude" to 1,
                    "longitude" to 1,
                    "imageURL" to downloadUri?.toString()
                )

                db.collection("reports")
                    .add(reportData)
                    .addOnSuccessListener { documentReference ->
                        Log.d("AddReportActivity", "Report saved with ID: ${documentReference.id}")
                        println("SHAY: db sent")
                        // Optionally, display a success message or navigate to another activity
                    }
                    .addOnFailureListener { e ->
                        Log.w("AddReportActivity", "Error adding report", e)
                        println("SHAY: db fail")
                        // Optionally, display an error message
                    }
                withContext(Dispatchers.Main) {
                    println("Upload Success")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Upload failed: ${e.message}")
                }
            }

        }

    }

    fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set up map interactions
        mMap.setOnMapClickListener { latLng ->
            // Clear existing markers
            mMap.clear()
            // Add a marker at the clicked location
            mMap.addMarker(MarkerOptions().position(latLng))
            // Save the selected location
            selectedLocation = latLng
        }

        // If a location is selected, display the marker
        selectedLocation?.let {
            mMap.addMarker(MarkerOptions().position(it))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }
}