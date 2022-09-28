package com.internship.move.feature.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface.BOLD
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.Settings
import android.view.View
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.internship.move.R
import com.internship.move.databinding.BottomSheetRideInfoCardBinding
import com.internship.move.databinding.BottomSheetScooterStartRideBinding
import com.internship.move.databinding.BottomSheetScooterUnlockOptionsCardBinding
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.authentication.AuthenticationViewModel
import com.internship.move.feature.scooter_unlock.ScooterStateViewModel
import com.internship.move.utils.bitmapDescriptorFromVector
import com.internship.move.utils.logTag
import com.internship.move.utils.showAlerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val authViewModel: AuthenticationViewModel by viewModel()
    private val scooterViewModel: ScooterViewModel by viewModel()
    private val scooterStateViewModel: ScooterStateViewModel by sharedViewModel()
    private lateinit var scooterMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var doubleBackPressed = false
    private lateinit var clusterManager: ClusterManager<MarkerItem>
    private val locationPermissionRequest = initPermissions()
    private var selectedMarker: Marker? = null
    private lateinit var currentLocation: LatLng
    private var rideStarted = false
    private var ongoingRide = false
    private var pauseOffset: Long = 0
    private var currentRideDistance: Int = 0

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
        println()
    }

    private fun initViews() {
        binding.scooterMapView.getMapAsync(this)
    }

    private fun initClustering() {
        clusterManager = ClusterManager<MarkerItem>(requireContext(), scooterMap)
        clusterManager.setAnimation(false)
        clusterManager.renderer = ScooterClusterRenderer(requireContext(), scooterMap, clusterManager)
        clusterManager.setOnClusterItemClickListener(initOnClusterItemClickListener())
    }

    private fun initObservers() {
        scooterViewModel.scooterList.observe(viewLifecycleOwner) { scooterList ->
            if (scooterList.isNotEmpty()) {
                addClusteredMarkers(scooterList)
            }
        }

        scooterStateViewModel.scooterResult.observe(viewLifecycleOwner) { scooterResult ->
            if (scooterResult != null) {
                showStartRideBottomSheetDialog(scooterResult)
            }
        }

        scooterStateViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (!isError.isNullOrEmpty())
                showAlerter(isError.toString(), requireActivity())
        }

        scooterStateViewModel.rideDistance.observe(viewLifecycleOwner) { rideDistance ->
            currentRideDistance = rideDistance
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

        binding.updateLocationBtn.setOnClickListener {
            getLocation()
        }

        binding.openMenuBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Work in progress", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initOnClusterItemClickListener() = ClusterManager.OnClusterItemClickListener<MarkerItem> { item ->
        try {
            if (selectedMarker != null) {
                selectedMarker?.setIcon(bitmapDescriptorFromVector(R.drawable.ic_map_pin, requireContext()))
            }
        } catch (e: IllegalArgumentException) {
            logTag("IN_CLUSTER", e.toString())
        }

        selectedMarker = (clusterManager.renderer as DefaultClusterRenderer<MarkerItem>).getMarker(item)
        if (selectedMarker != null) {
            scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker!!.position, ZOOM_LEVEL))
            selectedMarker?.setIcon(bitmapDescriptorFromVector(R.drawable.ic_selected_map_pin, requireContext()))

            if (item != null) {
                initScooterInfoCardView(item.title)
            }
        }
        true
    }

    private fun showUnlockScooterBottomSheetDialog(scooter: ScooterResponseDTO) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        val dialogBinding = BottomSheetScooterUnlockOptionsCardBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = getString(R.string.scooter_number_text, scooter.scooterNumber)
        dialogBinding.batteryLevelTV.text = scooter.battery
        setBatteryIcon(scooter.battery.toInt(), dialogBinding.batteryIndicatorIV)
        dialogBinding.unlockByCodeBtn.setOnClickListener {
            bottomSheetDialog.hide()
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToUnlockByCodeFragment())
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun showStartRideBottomSheetDialog(scooter: ScooterResponseDTO) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        val dialogBinding = BottomSheetScooterStartRideBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = getString(R.string.scooter_number_text, scooter.scooterNumber)
        dialogBinding.batteryLevelTV.text = getString(R.string.scooter_battery_level_text, scooter.battery)
        setBatteryIcon(scooter.battery.toInt(), dialogBinding.batteryIndicatorIV)
        dialogBinding.startRideBtn.setOnClickListener {
            rideStarted = true
            bottomSheetDialog.dismiss()
            scooterStateViewModel.startScooterRide()
            if (scooterStateViewModel.isError.value.isNullOrEmpty() && scooterStateViewModel.scooterResult.value != null) {
                showOngoingRideCardView(scooterStateViewModel.scooterResult.value!!)
            }
        }
        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
        val job = GlobalScope.launch(Dispatchers.Main) {
            coroutineScope {
                delay(30000L)
                showAlerter("Your reservation has expired!", requireActivity())
                scooterStateViewModel.resetScooterState()
                bottomSheetDialog.hide()
            }
        }
        bottomSheetDialog.setOnDismissListener {
            job.cancel()
            if (!rideStarted) {
                scooterStateViewModel.resetScooterState()
            }
        }
    }

    private fun showOngoingRideCardView(scooter: ScooterResponseDTO) {
        if (scooterStateViewModel.isError.value.isNullOrEmpty()) {
            val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
            val dialogBinding = BottomSheetRideInfoCardBinding.inflate(layoutInflater, null, false)
            dialogBinding.batteryLevelTV.text = scooter.battery
            setBatteryIcon(scooter.battery.toInt(), dialogBinding.batteryIndicatorIV)

            dialogBinding.endRideBtn.setOnClickListener {
                scooterStateViewModel.endScooterRIde()
                bottomSheetDialog.dismiss()
                dialogBinding.travelTimeChrono.base = SystemClock.elapsedRealtime()
                getLocation()
            }
            dialogBinding.pauseRideBtn.setOnClickListener {
                if (!ongoingRide) {
                    dialogBinding.pauseRideBtn.text = getString(R.string.lock_ride_btn_text)
                    dialogBinding.pauseRideBtn.setCompoundDrawables(
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock),
                        null,
                        null,
                        null
                    )
                    startChronometer(dialogBinding.travelTimeChrono)
                    scooterStateViewModel.unlockScooterRide()
                } else {
                    pauseChronometer(dialogBinding.travelTimeChrono)
                    scooterStateViewModel.lockScooterRide()
                    dialogBinding.pauseRideBtn.text = getString(R.string.unlock_ride_btn_text)
                    dialogBinding.pauseRideBtn.setCompoundDrawables(
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_unlock),
                        null,
                        null,
                        null
                    )
                }
            }
            bottomSheetDialog.setContentView(dialogBinding.root)
            bottomSheetDialog.show()
            initChronometer(dialogBinding.travelTimeChrono, dialogBinding.distanceTV)
            startChronometer(dialogBinding.travelTimeChrono)
        }

    }

    private fun initChronometer(chronometer: Chronometer, distanceTv: TextView) {
        chronometer.setOnChronometerTickListener {
            val time: Long = SystemClock.elapsedRealtime() - it.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val hh = if (h < 10) "0$h" else h.toString() + ""
            val mm = if (m < 10) "0$m" else m.toString() + ""
            val ss = if (s < 10) "0$s" else s.toString() + ""
            it.text = "$mm:$ss min" // will leave this as mm:ss format for now to check if it works
            if (ss.toInt() % 10 == 0) {
                getLocation()
                scooterStateViewModel.updateRideLocation(currentLocation)
                distanceTv.setTypeface(distanceTv.typeface, BOLD)
                distanceTv.text = currentRideDistance.toString()
            }
        }
    }

    private fun startChronometer(chronometer: Chronometer) {
        if (!ongoingRide) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            ongoingRide = true
        }
    }

    private fun pauseChronometer(chronometer: Chronometer) {
        if (ongoingRide) {
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            ongoingRide = false
        }
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
                    showUnlockScooterBottomSheetDialog(scooter)
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
        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CLUJANGELES, ZOOM_LEVEL))
        scooterMap.setOnMarkerClickListener(clusterManager)
        scooterMap.setOnCameraIdleListener { clusterManager.onCameraIdle() }
        scooterMap.setOnMapClickListener {
            if (binding.scooterInfo.scooterCV.isVisible) {
                binding.scooterInfo.scooterCV.visibility = View.GONE
            }
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
                        currentLocation = LatLng(list[0].latitude, list[0].longitude)
                        updateLocationComponents(currentLocation)
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

    private fun updateLocationComponents(location: LatLng) {
        scooterMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL))
        scooterViewModel.getScooters(
            location.latitude.toFloat(), location.longitude.toFloat()
        )

        binding.cityTv.text =
            Geocoder(requireContext(), Locale.getDefault()).getFromLocation(location.latitude, location.longitude, 1)[0].locality.toString()

        scooterMap.addCircle(
            CircleOptions().center(location)
                .fillColor(ColorUtils.setAlphaComponent(resources.getColor(R.color.primary_dark_purple, null), 26)).radius(30.0)
                .strokeWidth(0.0f)
        )

        scooterMap.addMarker(
            MarkerOptions().position(location).icon(bitmapDescriptorFromVector(R.drawable.ic_live_location, requireContext()))
                .anchor(.5f, .5f)
        )
    }

    private fun getScooterAddress(scooterCoords: CoordinatesDTO): String {
        try {
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
        } catch (e: Exception) {
            logTag("GetAddressException", e.toString())
            return getString(R.string.address_exception_location_text)
        }
    }

    companion object {
        private val CLUJANGELES = LatLng(46.770439, 23.591423)
        private const val ZOOM_LEVEL = 18.0f
        private const val ON_BACK_RESET_DURATION = 3000L

    }
}