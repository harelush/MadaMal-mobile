package com.harelshaigal.madamal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.harelshaigal.madamal.data.AppLocalDb
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val reportDao by lazy { AppLocalDb.db.reportDao()!! } // Initialize ReportDao lazily

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        // TODO: (Gal): remove
        // DB access
//        val report = Report(
//            id = 2, // Set the id according to your logic
//            ownerId = 123, // Set the ownerId according to your logic
//            data = "HARELUSH data",
//            lat = 0.0, // Set the latitude according to your logic
//            lng = 0.0, // Set the longitude according to your logic
//            image = "sample_image.jpg" // Set the image path according to your logic
//        )
//
//        // Insert the report into the database using a coroutine
//        GlobalScope.launch(Dispatchers.IO) {
//            reportDao.insertReports(report) // Corrected to insertReports(vararg reports: Report)
//            // Fetch all reports from the database
//            val allReports = reportDao.getAllReports()
//            println("All reports:")
//            allReports.collect { Log.d("SHAY", it.toString()) }
//        }
    }
}