package com.internship.move.feature.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerItem(
    private val lat: Double,
    private val long: Double,
    private val title: String,
    private val snippet: String
) : ClusterItem {

    override fun getPosition() = LatLng(lat, long)

    override fun getTitle() = title

    override fun getSnippet() = snippet
}