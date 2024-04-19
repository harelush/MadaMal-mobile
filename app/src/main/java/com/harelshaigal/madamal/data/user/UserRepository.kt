package com.harelshaigal.madamal.data.user

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.harelshaigal.madamal.data.AppLocalDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")
    private val userDao by lazy { AppLocalDb.db.userDao()!! }

    private lateinit var userRegistration: ListenerRegistration
    suspend fun startUserFetching(id: String?) = withContext(Dispatchers.IO) {
        if (id != null)
            userRegistration = usersCollection.document(id)
                .addSnapshotListener { documentSnapshot: DocumentSnapshot?, _: FirebaseFirestoreException? ->
                    if (documentSnapshot?.exists() == true) {
                        val userFromDb = documentSnapshot.toObject(UserDto::class.java)
                        userDao.insert(userDtoToUser(userFromDb!!))
                    }
                }
    }

    fun endUserFetching() = userRegistration.remove()
    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        saveUserInFirebase(user)
    }

    private fun saveUserInFirebase(user: User) {
        usersCollection.document(user.uid).set(user)
    }

    fun getUserById(id: String): LiveData<User?> = userDao.getUserById(id)

    suspend fun updateReport(user: User) = withContext(Dispatchers.IO) {
        saveUserInFirebase(user)
        userDao.updateUser(user)
    }

    private fun userDtoToUser(userDto: UserDto): User =
        User(
            uid = userDto.uid!!,
            fullName = userDto.fullName,
            email = userDto.email,
            imageUri = userDto.imageUri
        )

}