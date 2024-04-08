package com.harelshaigal.madamal.ui.userDispaly.userProfile

import OperationStatus
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.harelshaigal.madamal.data.User
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private val _updateStatus = MutableLiveData<OperationStatus>()
    val updateStatus: LiveData<OperationStatus> = _updateStatus

    fun fetchUserData(uid: String) {
        db.collection("users").document(uid).get().addOnSuccessListener { document ->
            if (document != null) {
                val user = document.toObject(User::class.java)
                _user.value = user
            } else {
                _user.value = null
            }
        }.addOnFailureListener { exception ->
            _user.value = null
        }
    }

    fun fetchUserImageUrl(uid: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$uid/profile.jpg")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            _imageUrl.postValue(uri.toString())
        }.addOnFailureListener {
            _imageUrl.postValue("")
        }
    }

    fun uploadUserImage(imageUri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userUid = Firebase.auth.currentUser?.uid
                if (userUid != null) {
                    val fileName = "images/$userUid/profile.jpg"
                    ImagePickerHelper.uploadImageToFirebaseStorage(
                        imageUri,
                        fileName,
                        null // Context is not used in this example, pass null or adjust accordingly.
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateUserDetails(uid: String, fullName: String) {
        _updateStatus.value = OperationStatus.LOADING
        db.collection("users").document(uid).update("fullName", fullName)
            .addOnSuccessListener {
                _updateStatus.postValue(OperationStatus.SUCCESS)
            }
            .addOnFailureListener { e ->
                _updateStatus.postValue(OperationStatus.FAILURE)
            }
    }

}