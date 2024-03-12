package com.harelshaigal.madamal.ui.reportsMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.harelshaigal.madamal.R
import com.harelshaigal.madamal.databinding.FragmentReportsMapBinding

class ReportsMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: ReportsMapViewModel

    private var _binding: FragmentReportsMapBinding? = null

    private lateinit var mapView: MapView

    private var googleMapRef: GoogleMap? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[ReportsMapViewModel::class.java]

        _binding = FragmentReportsMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return root
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapRef = googleMap

        val location = LatLng(31.97007377827919, 34.772878313889215)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }
}