package com.harelshaigal.madamal.ui.mapDisplay.reportsMap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.databinding.FragmentReportsMapBinding
import com.harelshaigal.madamal.ui.mapDisplay.reportMapDisplay.ReportMapDisplayFragment
import com.harelshaigal.madamal.ui.reportsList.ReportsListViewModel

class ReportsMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var viewModel: ReportsListViewModel

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
            ViewModelProvider(this)[ReportsListViewModel::class.java]

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

        observeReports()

        val location = LatLng(31.97007377827919, 34.772878313889215)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        googleMap.setOnMarkerClickListener(this)
    }

    private fun observeReports() {
        viewModel.getReportList(null).observe(viewLifecycleOwner) { reports ->
            updateMapMarkers(reports)
        }
    }

    private fun updateMapMarkers(reports: List<Report>) {
        for (report in reports) {
            if (report.lat != null && report.lng != null) {
                val reportMarker = LatLng(report.lat, report.lng)
                val markerOptions: MarkerOptions =
                    MarkerOptions().position(reportMarker).title(report.data)

                googleMapRef?.addMarker(markerOptions)?.tag = report
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.let {
            ReportMapDisplayFragment.display(getParentFragmentManager(), it.tag as Report)
            return true
        }
    }
}