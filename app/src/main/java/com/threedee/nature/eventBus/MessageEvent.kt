package com.threedee.nature.eventBus

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class MessageEvent (
    val latLng: ArrayList<Location>
)