package com.adrcotfas.locationexplorer.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotoUrlEntity::class], version = 1, exportSchema = false)
abstract class PhotoUrlDatabase : RoomDatabase() {
    abstract fun photoUrlDao(): PhotoUrlDao

    companion object {
        const val DATABASE_NAME: String = "photo_url_db"
    }
}