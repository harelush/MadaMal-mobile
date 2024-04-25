package com.harelshaigal.madamal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.data.LocationDataViewModel
import com.harelshaigal.madamal.data.report.ReportRepository
import com.harelshaigal.madamal.data.user.UserRepository
import com.harelshaigal.madamal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationViewModel: LocationDataViewModel
    private val userRepository: UserRepository = UserRepository()
    private val reportRepository: ReportRepository = ReportRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationViewModel = ViewModelProvider(this).get(LocationDataViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        userRepository.startUserFetching(Firebase.auth.currentUser?.uid)
        reportRepository.startReportsFetching()
    }

    override fun onDestroy() {
        super.onDestroy()
        userRepository.endUserFetching()
        reportRepository.endReportsFetching()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations, this can be null.
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    locationViewModel.latitude = latitude
                    locationViewModel.longtitude = longitude
                }
            }
            .addOnFailureListener { e ->
            }
    }
}