package com.adrcotfas.locationexplorer.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoUrlDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: PhotoUrlEntity)

    @Query("SELECT * FROM photos")
    fun get(): LiveData<List<PhotoUrlEntity>>

    @Query("DELETE FROM photos")
    suspend fun clear()
}