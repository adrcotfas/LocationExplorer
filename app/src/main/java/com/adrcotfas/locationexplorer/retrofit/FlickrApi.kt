package com.adrcotfas.locationexplorer.retrofit

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FlickrApi {

    companion object {
        const val BASE_URL = "https://api.flickr.com/services/rest/"
        const val API_KEY = "f32db9ea2c5d5ed8388b991228952584"
        const val FORMAT = "json"
        const val METHOD_PHOTO_SEARCH = "flickr.photos.search"
        const val METHOD_PHOTO_LOCATION = "flickr.photos.geo.getLocation"
        const val DEFAULT_RADIUS = "0.1" // [km]
    }

    @GET(".")
    suspend fun fetchPhotos(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = FORMAT,
        @Query("method") method: String = METHOD_PHOTO_SEARCH,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("radius") radius: String = DEFAULT_RADIUS,
        @Query("per_page") perPage: String = "30",
        @Query("nojsoncallback") noJsonCallback: String = "1",
    ): FlickrResponse

    @Headers("Accept: application/json")
    @GET(".")
    suspend fun fetchPhotoLocation(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = FORMAT,
        @Query("method") method: String = METHOD_PHOTO_LOCATION,
        @Query("photo_id") photoId: String,
        @Query("nojsoncallback") noJsonCallback: String = "1",
    ) : FlickrResponseWithLocation
}
