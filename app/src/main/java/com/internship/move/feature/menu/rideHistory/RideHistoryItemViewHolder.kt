package com.internship.move.feature.menu.rideHistory

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.internship.move.R
import com.internship.move.databinding.RideHistoryCardviewBinding
import com.internship.move.feature.map.CoordinatesDTO
import com.internship.move.utils.getScooterAddress
import com.internship.move.utils.logTag

class RideHistoryItemViewHolder(private val binding: RideHistoryCardviewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ride: RideHistoryItemDTO) {

        binding.travelDistanceTV.text = binding.root.context.getString(R.string.ride_distance_format_text, ride.distance.toFloat()/100)
        binding.travelTimeTV.text = binding.root.context.getString(R.string.travel_time_title_format_text, ride.duration / 60, ride.duration % 60)
        logTag("ScooterAddressStart", ride.coordinates[0].latitude.toString())
        logTag("ScooterAddressEnd", ride.coordinates[0].longitude.toString())
        logTag("ScooterAddressCoords", CoordinatesDTO(listOf(ride.coordinates[0].longitude, ride.coordinates[0].latitude)).toString())
        binding.startAddressTV.text =
            getScooterAddress(CoordinatesDTO(coordinates = listOf(ride.coordinates[0].longitude, ride.coordinates[0].latitude)), binding.root.context)
        binding.endAddressTV.text =
            getScooterAddress(CoordinatesDTO(coordinates = listOf(ride.coordinates[1].longitude, ride.coordinates[1].latitude)), binding.root.context)
    }
}
