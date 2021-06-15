package com.adrcotfas.locationexplorer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.adrcotfas.locationexplorer.room.PhotoUrlDatabase
import com.adrcotfas.locationexplorer.room.PhotoUrlEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val db: PhotoUrlDatabase) : ViewModel() {
    val photoUrls: LiveData<List<PhotoUrlEntity>> = db.photoUrlDao().get()
}