package com.adrcotfas.locationexplorer.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoUrlEntity (
    @PrimaryKey(autoGenerate = false)
    val url : String
)