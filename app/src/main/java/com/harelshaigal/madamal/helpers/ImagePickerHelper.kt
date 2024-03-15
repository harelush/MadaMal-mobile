package com.harelshaigal.madamal.helpers

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.storage
import com.harelshaigal.madamal.helpers.ToastHelper.showToast
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
            selectedImageUri: Uri,
            fileName: String,
            context: Context
        ): Uri? {
            val user = Firebase.auth.currentUser
            return if (user != null) {
                val ref = Firebase.storage.reference.child(fileName)
                try {
                    ref.putFile(selectedImageUri).await() // Upload the file
                    val downloadUri = ref.downloadUrl.await()
                    withContext(Dispatchers.Main) {
                    }
                    downloadUri
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showToast(
                            "Upload failed: ${e.message}",
                            context
                        ) // Handle failure, e.g., show a toast
                    }
                    null
                }
            } else {
                withContext(Dispatchers.Main) {
                    showToast("No user logged in or no image selected", context)
                }
                null
            }
        }
    }
}