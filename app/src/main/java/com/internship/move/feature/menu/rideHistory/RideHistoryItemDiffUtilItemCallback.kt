package com.internship.move.feature.menu.rideHistory

import androidx.recyclerview.widget.DiffUtil

class RideHistoryItemDiffUtilItemCallback : DiffUtil.ItemCallback<RideHistoryItemDTO>() {
    override fun areItemsTheSame(oldItem: RideHistoryItemDTO, newItem: RideHistoryItemDTO) = oldItem == newItem

    override fun areContentsTheSame(oldItem: RideHistoryItemDTO, newItem: RideHistoryItemDTO) = oldItem == newItem

}
