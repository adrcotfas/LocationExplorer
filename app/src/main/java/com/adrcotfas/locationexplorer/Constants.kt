package com.adrcotfas.locationexplorer

import android.location.Location

const val START = "explorer.START"
const val STOP = "explorer.STOP"

fun Location?.toText(): String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}