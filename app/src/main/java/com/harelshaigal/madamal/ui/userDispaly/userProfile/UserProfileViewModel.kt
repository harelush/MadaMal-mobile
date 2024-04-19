package com.harelshaigal.madamal.ui.userDispaly.userProfile

import OperationStatus
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.data.user.User
import com.harelshaigal.madamal.data.user.UserRepository
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.harelshaigal.madamal.helpers.Utils

class UserProfileViewModel : ViewModel() {

    private val repository: UserRepository = UserRepository()
    lateinit var user: LiveData<User?>

    private val _updateStatus = MutableLiveData<OperationStatus>()
    val updateStatus: LiveData<OperationStatus> = _updateStatus

    fun fetchUserData() {
        user = repository.getUserById(Firebase.auth.currentUser!!.uid)
    }

    suspend fun updateUserDetails(fullName: String, selectedImageUri: Uri?) {
        _updateStatus.postValue(OperationStatus.LOADING)

        try {
            val userDataToUpdate: User = user.value!!.copy(fullName = fullName)

            if (selectedImageUri != null) {
                userDataToUpdate.imageUri = ImagePickerHelper.uploadImageToFirebaseStorage(
                    selectedImageUri,
                    Utils.getUserImageName(userDataToUpdate.uid)
                ).toString()
            }

            repository.updateReport(userDataToUpdate)
            _updateStatus.postValue(OperationStatus.SUCCESS)
        } catch (e: Exception) {
            _updateStatus.postValue(OperationStatus.FAILURE)
        }
    }
}