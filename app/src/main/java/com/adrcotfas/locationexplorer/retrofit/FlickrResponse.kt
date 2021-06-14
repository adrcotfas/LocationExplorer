package com.adrcotfas.locationexplorer.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class FlickrResponse(
    @SerializedName("photos")
    var photos: Photos
)

data class Photos(
    @SerializedName("page")
    var page: Int,
    @SerializedName("pages")
    var pages: Int,
    @SerializedName("perpage")
    var perPage: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("photo")
    var photos: List<Photo>
)

@Parcelize
data class Photo(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("secret")
    var secret: String? = null,
    @SerializedName("server")
    var server: String? = null,
    @SerializedName("farm")
    var farm: Int? = 0,
) : Parcelable

fun Photo.getImageUrl(): String {
    return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}.jpg"
}