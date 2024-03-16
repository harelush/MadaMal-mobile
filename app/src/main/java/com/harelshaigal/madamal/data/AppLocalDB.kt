package com.harelshaigal.madamal.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harelshaigal.madamal.application.MadamalApplication


@Database(entities = [Report::class], version = 1)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun reportDao(): ReportDao?
}


object AppLocalDb {
    var db = Room.databaseBuilder(
        MadamalApplication.context,
        AppLocalDbRepository::class.java,
        "dbFileName.db"
    )
        .fallbackToDestructiveMigration()
        .build()
}