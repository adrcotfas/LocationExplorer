package com.adrcotfas.locationexplorer.retrofit


import com.google.gson.annotations.SerializedName

data class FlickrResponseWithLocation(
    @SerializedName("photo")
    val photo: PhotoWithLocation,
    @SerializedName("stat")
    val stat: String
)

data class PhotoWithLocation(
    @SerializedName("id")
    val id: String,
    @SerializedName("location")
    val location: Location
)

data class Location(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("accuracy")
    val accuracy: String,
    @SerializedName("context")
    val context: String,
    @SerializedName("county")
    val county: County,
    @SerializedName("region")
    val region: Region,
    @SerializedName("country")
    val country: Country,
    @SerializedName("neighbourhood")
    val neighbourhood: Neighbourhood
)

data class Country(
    @SerializedName("_content")
    val content: String
)

data class County(
    @SerializedName("_content")
    val content: String
)

data class Region(
    @SerializedName("_content")
    val content: String
)

data class Neighbourhood(
    @SerializedName("_content")
    val content: String
)
