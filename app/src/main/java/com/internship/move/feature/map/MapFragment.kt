package com.internship.move.feature.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.ClusterManager
import com.internship.move.R
import com.internship.move.databinding.BottomSheetScooterCardBinding
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.utils.bitmapDescriptorFromVector
import com.internship.move.utils.logTag
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val authViewModel: AuthenticationViewModel by viewModel()
    private val scooterViewModel: ScooterViewModel by viewModel()
    private lateinit var scooterMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var doubleBackPressed = false
    private lateinit var clusterManager: ClusterManager<MarkerItem>
    private val locationPermissionRequest = initPermissions()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getCurrentUser()
        logTag("MAP_USER", authViewModel.getCurrentUser().toString())

        binding.scooterMapView.onCreate(savedInstanceState)
        binding.scooterMapView.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLocation()
        initViews()
        initButtons()
        initObservers()
    }

    private fun initViews() {
        binding.scooterMapView.getMapAsync(this)
    }

    private fun initClustering() {
        clusterManager = ClusterManager<MarkerItem>(requireContext(), scooterMap)
        clusterManager.setAnimation(false)
        clusterManager.renderer = ScooterClusterRenderer(requireContext(), scooterMap, clusterManager)
        clusterManager.setOnClusterItemClickListener {
//            Marker(it.position).remove()
//            MarkerOptions().title(it.title).position(it.position).icon(bitmapDescriptorFromVector(R.drawable.ic_selected_map_pin, requireContext()))
            initScooterInfoCardView(it.title)
            return@setOnClusterItemClickListener false
        }
    }

    private fun initObservers() {
        scooterViewModel.scooterList.observe(viewLifecycleOwner) { scooterList ->
            if (scooterList.isNotEmpty()) {
                addClusteredMarkers(scooterList)
            }
        }

        scooterViewModel.currentLocation.observe(viewLifecycleOwner)
        { location ->
            scooterMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            scooterViewModel.getScooters(location.latitude.toFloat(), location.longitude.toFloat())
        }
    }

    private fun initButtons() {
        requireActivity().onBackPressedDispatcher.addCallback {
            if (doubleBackPressed) {
                authViewModel.logOut()
                requireActivity().finish()
            } else {
                doubleBackPressed = true
                Toast.makeText(requireContext(), getString(R.string.exit_app_button_text), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackPressed = false
                }, ON_BACK_RESET_DURATION)
            }
        }
    }

    private fun showBottomSheetDialog(scooter: ScooterResponseDTO) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        val dialogBinding = BottomSheetScooterCardBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = getString(R.string.scooter_number_text, scooter.scooterNumber)
        dialogBinding.batteryLevelTV.text = scooter.battery
        setBatteryIcon(scooter.battery.toInt(), dialogBinding.batteryIndicatorIV)

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun addClusteredMarkers(scooterList: List<ScooterResponseDTO>) {
        clusterManager.clearItems()
        clusterManager.addItems(scooterViewModel.getMarkerItemsList(scooterList))
        clusterManager.cluster()
    }

    private fun initScooterInfoCardView(markerTitle: String) {
        val scooter = scooterViewModel.getScooterById(markerTitle)
        if (scooter != null) {
            binding.apply {
                scooterInfo.scooterCV.isVisible = true
                scooterInfo.unlockScooterBtn.text = getString(R.string.unlock_scooter_string)
                scooterInfo.scooterNumberTV.text = getString(R.string.scooter_number_text, scooter.scooterNumber)
                scooterInfo.batteryLevelTV.text = getString(R.string.scooter_battery_level_text, scooter.battery)
                scooterInfo.scooterLocationTV.text = getScooterAddress(scooter.location)
                binding.scooterInfo.unlockScooterBtn.setOnClickListener {
                    showBottomSheetDialog(scooter)
                }
            }
            setBatteryIcon(scooter.battery.toInt(), binding.scooterInfo.batteryIndicatorIV)
        } else {
            binding.scooterInfo.scooterCV.isVisible = false
            Toast.makeText(requireContext(), "No scooter here!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBatteryIcon(batteryLevel: Int, imageView: ImageView) {
        val resource = when {
            batteryLevel >= 81 -> R.drawable.ic_battery_100
            batteryLevel >= 61 -> R.drawable.ic_battery_80
            batteryLevel >= 41 -> R.drawable.ic_battery_60
            batteryLevel >= 21 -> R.drawable.ic_battery_40
            batteryLevel >= 4 -> R.drawable.ic_battery_20
            batteryLevel >= 0 -> R.drawable.ic_battery_0
            else -> R.drawable.ic_battery_0
        }
        imageView.setImageResource(resource)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        scooterMap = googleMap
        initClustering()
        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(CLUJANGELES))
        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CLUJANGELES, ZOOM_LEVEL))
        scooterMap.setOnMarkerClickListener(clusterManager)
        scooterMap.setOnCameraIdleListener { clusterManager.onCameraIdle() }
        scooterMap.setOnMapClickListener {
            binding.scooterInfo.scooterCV.visibility = View.GONE
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(GPS_PROVIDER) || locationManager.isProviderEnabled(
            NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                ACCESS_FINE_LOCATION
            ),
        )
    }

    private fun initPermissions() = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                getLocation()
            }
            else -> {
                Toast.makeText(requireContext(), "Permissions not given", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        scooterViewModel.currentLocation.value = LatLng(list[0].latitude, list[0].longitude)
                        val currentLocation = scooterViewModel.currentLocation.value ?: CLUJANGELES
                        scooterMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL))
                        scooterViewModel.getScooters(
                            currentLocation.latitude.toFloat(), currentLocation.longitude.toFloat()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.turn_location_message_string), Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun getScooterAddress(scooterCoords: CoordinatesDTO): String {
        return getString(
            R.string.scooter_address_string, Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
                scooterCoords.coordinates[1],
                scooterCoords.coordinates[0],
                1
            )[0].thoroughfare
                .toString(), Geocoder(requireContext(), Locale.getDefault()).getFromLocation(
                scooterCoords.coordinates[1],
                scooterCoords.coordinates[0],
                1
            )[0].subThoroughfare
                .toString()
        )
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)
        private const val ZOOM_LEVEL = 18.0f
        private const val ON_BACK_RESET_DURATION = 3000L

    }
}