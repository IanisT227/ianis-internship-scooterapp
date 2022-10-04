package com.internship.move.feature.menu.rideHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.internship.move.databinding.RideHistoryCardviewBinding

class RideHistoryAdapter() :
    ListAdapter<RideHistoryItemDTO, RideHistoryItemViewHolder>(RideHistoryItemDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RideHistoryItemViewHolder(
        RideHistoryCardviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RideHistoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}