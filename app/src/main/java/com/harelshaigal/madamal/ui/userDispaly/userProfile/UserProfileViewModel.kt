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
import com.harelshaigal.madamal.data.report.ReportRepository
import com.harelshaigal.madamal.data.user.User
import com.harelshaigal.madamal.data.user.UserRepository
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.harelshaigal.madamal.helpers.Utils
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {

    private val repository: UserRepository = UserRepository()

//    private var _user = MutableLiveData<User?>()
//    var user: LiveData<User?> = _user
    lateinit var user: LiveData<User?>

    private val _updateStatus = MutableLiveData<OperationStatus>()
    val updateStatus: LiveData<OperationStatus> = _updateStatus

    fun fetchUserData(uid: String) {
        user = repository.getUserById(uid)
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