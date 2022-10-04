package com.internship.move.feature.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.internship.move.R
import com.internship.move.databinding.FragmentTripDetailsBinding
import com.internship.move.feature.scooter_unlock.ScooterStateViewModel
import com.internship.move.utils.bitmapDescriptorFromVector
import com.internship.move.utils.getScooterAddress
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TripDetailsFragment : Fragment(R.layout.fragment_trip_details), OnMapReadyCallback {
    private val binding by viewBinding(FragmentTripDetailsBinding::bind)
    private val scooterStateViewModel: ScooterStateViewModel by sharedViewModel()
    private lateinit var tripMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.endRideMapView.onCreate(savedInstanceState)
        binding.endRideMapView.onResume()
        initButtons()
        initMapAndViews()
    }

    private fun initButtons() {
        binding.payTripBtn.setOnClickListener {
            scooterStateViewModel.finishRidePayment()
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.dialog_title))
                .setMessage(resources.getString(R.string.dialog_supporting_text))
                .setPositiveButton(resources.getString(R.string.dialog_positive_text)) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    private fun initMapAndViews() {
        binding.endRideMapView.getMapAsync(this)
        val coordinatesList = scooterStateViewModel.lastRide.value?.coordinates
        binding.startAddressTV.text = getScooterAddress(
            CoordinatesDTO(
                listOf(
                    coordinatesList?.get(0)?.longitude ?: CLUJANGELES.longitude,
                    coordinatesList?.get(0)?.latitude ?: CLUJANGELES.latitude
                )
            ), requireContext()
        )
        logTag("CoordinatesList", coordinatesList.toString())
        binding.endAddressTV.text = getScooterAddress(
            CoordinatesDTO(
                listOf(
                    coordinatesList?.last()?.longitude ?: CLUJANGELES.longitude,
                    coordinatesList?.last()?.latitude ?: CLUJANGELES.latitude
                )
            ), requireContext()
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        tripMap = googleMap
        val options = PolylineOptions()
        options.color(context?.resources?.getColor(R.color.accent_pink) ?: R.color.accent_pink)
        options.width(15f)
        for (coordinate in scooterStateViewModel.lastRide.value?.coordinates!!)
            options.add(LatLng(coordinate.latitude, coordinate.longitude))
        tripMap.addPolyline(options)
        val coordinates = scooterStateViewModel.lastRide.value?.coordinates
        tripMap.addMarker(
            MarkerOptions().icon(bitmapDescriptorFromVector(R.drawable.ic_live_location, requireContext())).position(
                LatLng(
                    coordinates?.first()?.latitude ?: CLUJANGELES.latitude, coordinates?.first()?.longitude ?: CLUJANGELES.longitude
                )
            )
        )

        tripMap.addMarker(
            MarkerOptions().icon(bitmapDescriptorFromVector(R.drawable.ic_map_pin, requireContext())).position(
                LatLng(
                    coordinates?.last()?.latitude ?: CLUJANGELES.latitude, coordinates?.last()?.longitude ?: CLUJANGELES.longitude
                )
            )
        )
        tripMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    scooterStateViewModel.lastRide.value?.coordinates?.get(
                        scooterStateViewModel.lastRide.value?.coordinates?.size?.div(2) ?: 0
                    )?.latitude
                        ?: CLUJANGELES.latitude,
                    scooterStateViewModel.lastRide.value?.coordinates?.get(
                        scooterStateViewModel.lastRide.value?.coordinates?.size?.div(2) ?: 0
                    )?.longitude
                        ?: CLUJANGELES.longitude
                ),
                ZOOM_LEVEL
            )
        )
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)
        private const val ZOOM_LEVEL = 16.0f
    }
}