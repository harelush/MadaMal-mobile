package com.harelshaigal.madamal.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harelshaigal.madamal.MyApplicationSingleton


@Database(entities = [Report::class], version = 1)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun reportDao(): ReportDao?
}


object AppLocalDb {
    var db = Room.databaseBuilder(
        MyApplicationSingleton.getContext(),
        AppLocalDbRepository::class.java,
        "dbFileName.db"
    )
        .fallbackToDestructiveMigration()
        .build()
}