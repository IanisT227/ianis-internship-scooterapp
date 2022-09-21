package com.internship.move.feature.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.internship.move.R
import com.internship.move.utils.bitmapDescriptorFromVector

class ScooterClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MarkerItem>
) : DefaultClusterRenderer<MarkerItem>(context, map, clusterManager) {

    private val mapPinIcon: BitmapDescriptor by lazy {
        bitmapDescriptorFromVector(
            R.drawable.ic_map_pin,
            context
        )
    }

    override fun onBeforeClusterItemRendered(
        item: MarkerItem,
        markerOptions: MarkerOptions
    ) {
        markerOptions.title(item.title)
            .position(item.position)
            .icon(mapPinIcon)
    }

    override fun onClusterItemRendered(clusterItem: MarkerItem, marker: Marker) {
        marker.tag = clusterItem
    }
}