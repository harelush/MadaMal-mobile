package com.harelshaigal.madamal.data.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM User WHERE uid = :uid")
    fun getUserById(uid: String): LiveData<User?>

    @Update
    fun updateUser(user: User)

}