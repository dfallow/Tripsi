package com.example.tripsi.screens.pastTrip

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.tripsi.R
import com.example.tripsi.data.Location
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.MomentPosition
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun PastTripMap(context: Context, tripDbViewModel: TripDbViewModel) {
    val pastTripMap = createMap()
    var mapInitialized by remember(pastTripMap) { mutableStateOf(false) }

    val distanceBetween = FloatArray(1)

    val pastTripLocations = tripDbViewModel.pastTripData.location
    if (pastTripLocations != null && pastTripLocations.isNotEmpty()) {
        android.location.Location.distanceBetween(
            pastTripLocations.first().coordsLatitude,
            pastTripLocations.first().coordsLongitude,
            pastTripLocations.last().coordsLatitude,
            pastTripLocations.last().coordsLongitude,
            distanceBetween
        )
    }

    var pastLocations: Array<Marker> = arrayOf(Marker(pastTripMap))
    val pastPolylinePoints: ArrayList<GeoPoint> = ArrayList()
    val polyline: Polyline

    fun createPastTripMoments(pastTripLocations: List<Location>) {
        for (location in pastTripLocations) {
            val pastMomentMarker = Marker(pastTripMap)
            pastMomentMarker.position = GeoPoint(location.coordsLatitude, location.coordsLongitude)
            pastMomentMarker.id = location.locationId

            if (location.position == MomentPosition.MIDDLE) {
                pastMomentMarker.icon = ContextCompat.getDrawable(context, R.drawable.photo_svgrepo_com)

                pastMomentMarker.setOnMarkerClickListener { _, _ ->
                    true
                }

            } else {
                pastMomentMarker.icon = ContextCompat.getDrawable(context, R.drawable.location_svgrepo_com)

                pastMomentMarker.setOnMarkerClickListener { _, _ ->
                    true
                }
            }
            pastLocations += pastMomentMarker
        }
    }

    if (pastTripLocations != null) {
        polyline = Polyline()

        for (pastLocation in pastTripLocations) {
            pastPolylinePoints.add(
                GeoPoint(pastLocation.coordsLatitude,
                    pastLocation.coordsLongitude
                )
            )
        }
        polyline.setPoints(pastPolylinePoints)
        createPastTripMoments(pastTripLocations)
    } else {
        polyline = Polyline()
        polyline.setPoints(pastPolylinePoints)
    }

    if (!mapInitialized) {
        pastTripMap.setTileSource(TileSourceFactory.MAPNIK)
        pastTripMap.setMultiTouchControls(true)

        when (distanceBetween[0].toInt()) {
            in 0..500 -> {pastTripMap.controller.setZoom(15.0)}
            in 501..5000 -> {pastTripMap.controller.setZoom(13.0)}
            in 5001..50000 -> {pastTripMap.controller.setZoom(11.0)}
            in 500001..5000000 -> {pastTripMap.controller.setZoom(8.0)}
            else -> {pastTripMap.controller.setZoom(5.0)}
        }
        pastTripMap.controller.setCenter(
            GeoPoint(pastTripLocations!![0].coordsLatitude, pastTripLocations[0].coordsLongitude)
        )
        mapInitialized = true
    }
    AndroidView({ pastTripMap }) {


        pastTripMap.overlays.add(polyline)

        for (pastMoment in pastLocations) {
            pastTripMap.overlays.add(pastMoment)
        }

    }
}

@Composable
fun createMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }
    return mapView
}