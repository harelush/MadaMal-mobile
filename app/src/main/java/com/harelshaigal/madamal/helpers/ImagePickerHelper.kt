package com.harelshaigal.madamal.helpers

import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.tasks.await

class ImagePickerHelper(fragment: Fragment, private val callback: ImagePickerCallback) {

    interface ImagePickerCallback {
        fun getImageViewForLoad(): ImageView
        fun selectedImageExtraLogic(uri: Uri?) {}
    }

    private val pickImageLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { selectedImageUri ->
            selectedImageUri?.let { uri ->
                loadImage(uri)
                callback.selectedImageExtraLogic(uri)
            }
        }

    fun openImagePicker() {
        pickImageLauncher.launch("image/*")
    }

    private fun loadImage(uri: Uri) {
        val imageView = this.callback.getImageViewForLoad()

        imageView.let {
            Picasso.get().load(uri).into(it)
        }
    }

    companion object {
        suspend fun uploadImageToFirebaseStorage(
            selectedImageUri: Uri?,
            fileName: String,
        ): Uri? {
            val user = Firebase.auth.currentUser
            if (user == null || selectedImageUri == null) {
                return null
            }
            val ref = Firebase.storage.reference.child(fileName)
            return try {
                ref.putFile(selectedImageUri).await() // Upload the file
                ref.downloadUrl.await() // Get download URL
            } catch (e: Exception) {
                null
            }
        }
    }

}