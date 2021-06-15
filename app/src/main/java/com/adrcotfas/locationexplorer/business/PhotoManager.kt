package com.adrcotfas.locationexplorer.business

import android.location.Location
import android.util.Log
import com.adrcotfas.locationexplorer.retrofit.FlickrApi
import com.adrcotfas.locationexplorer.retrofit.Photo
import com.adrcotfas.locationexplorer.retrofit.getImageUrl
import com.adrcotfas.locationexplorer.room.PhotoUrlDatabase
import com.adrcotfas.locationexplorer.room.PhotoUrlEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Exception
import javax.inject.Inject

class PhotoManager @Inject constructor(
    private val flickrApi: FlickrApi,
    private val db: PhotoUrlDatabase
) :
    LocationProvider.Listener {

    private var currentLocation = Location("")
    private var closestPhotoIdDistance = Float.MAX_VALUE
    private var closestPhotoUrl = ""

    /**
     * Use a mutex to avoid concurrent access to the closest location processing operation
     * in case of rapid location changes.
     */
    private val mutex = Mutex()

    /**
     * When a new location is received, fetch photos from that area.
     * The photos do not come sorted by proximity to our location
     * so we need an extra step to get the closest photo.
     * See [processLocation]
     */
    override fun onLocationResult(lat: Double, lon: Double) {
        Log.d(TAG, "New location: $lat / $lon")

        closestPhotoIdDistance = Float.MAX_VALUE
        closestPhotoUrl = ""

        currentLocation.apply {
            latitude = lat
            longitude = lon
        }

        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                try {
                    val photos = flickrApi.fetchPhotos(
                        lat = lat.toString(),
                        lon = lon.toString()
                    ).photos.photos
                    for (photo in photos) {
                        processLocation(photo)
                    }
                    if (closestPhotoUrl.isNotEmpty()) {
                        Log.d(TAG, "Shortest distance: $closestPhotoIdDistance m")
                        db.photoUrlDao().insert(PhotoUrlEntity(closestPhotoUrl))
                    }
                } catch (e: Exception) {
                    //TODO: implement proper error handling
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    /**
     * Fetch a photo's location, check its proximity to the current location
     * and update the closest photo URL if it's closer.
     */
    private suspend fun processLocation(photo: Photo) {
        val crtLocation = Location("").apply {
            val location = flickrApi.fetchPhotoLocation(photoId = photo.id!!).photo.location
            latitude = location.latitude.toDouble()
            longitude = location.longitude.toDouble()
        }
        val distance = currentLocation.distanceTo(crtLocation) // [m]
        Log.d(TAG, "Current distance: $distance m")
        if (distance < closestPhotoIdDistance) {
            closestPhotoIdDistance = distance
            closestPhotoUrl = photo.getImageUrl()
        }
    }

    companion object {
        private const val TAG = "PhotoManager"
    }
}