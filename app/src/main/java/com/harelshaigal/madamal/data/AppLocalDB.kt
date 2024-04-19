package com.harelshaigal.madamal.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harelshaigal.madamal.application.MadamalApplication
import com.harelshaigal.madamal.data.report.Report
import com.harelshaigal.madamal.data.report.ReportDao
import com.harelshaigal.madamal.data.user.User
import com.harelshaigal.madamal.data.user.UserDao


@Database(entities = [Report::class, User::class], version = 5)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun reportDao(): ReportDao?
    abstract fun userDao(): UserDao?
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