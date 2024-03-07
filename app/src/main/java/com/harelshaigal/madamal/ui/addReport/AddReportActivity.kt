package com.harelshaigal.madamal.ui.addReport

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.harelshaigal.madamal.databinding.ActivityAddReportBinding
import com.harelshaigal.madamal.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AddReportActivity : AppCompatActivity() {

    private var _binding: FragmentRegisterBinding? = null
    private val auth = Firebase.auth
    private lateinit var binding: ActivityAddReportBinding
    private var selectedImageUri: Uri? = null

    private lateinit var mMap: GoogleMap
    private var selectedLocation: LatLng? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root);

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding.addReportImageView.setImageURI(uri)
                    selectedImageUri = uri
                }
            }


        binding.addReportImageView.setOnClickListener {
            openGalleryForImage()
        }

        // Inside onCreate method of AddReportActivity.kt
        binding.saveButton.setOnClickListener {
            runBlocking {
                launch {
                    saveReport(binding.addReportContent.text.toString())
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private suspend fun saveReport(content: String) {
        val user = Firebase.auth.currentUser
        val uri = selectedImageUri!!
        val downloadUri: Uri;
        if(user != null) {
            val fileName = "reportsImages/${user.uid}/reportImage.jpg"
            val ref = Firebase.storage.reference.child(fileName)
            try {
                ref.putFile(uri).await() // Upload the file
                downloadUri = ref.downloadUrl.await()

                println("SHAY: uploaded image")
                val db = Firebase.firestore
                val reportData = hashMapOf(
                    "content" to content,
                    "userID" to user.uid,
                    "latitude" to 1,
                    "longitude" to 1,
                    "imageURL" to downloadUri.toString()
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



    private fun openGalleryForImage() {
        imagePickerLauncher.launch("image/*")
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