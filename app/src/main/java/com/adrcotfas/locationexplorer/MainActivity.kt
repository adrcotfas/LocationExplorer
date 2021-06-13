package com.adrcotfas.locationexplorer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.adrcotfas.locationexplorer.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val LOCATION_PERMISSION_REQUEST_CODE = 11

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButton()
        setupRecycler()
        requestForegroundPermissions()
    }

    private fun setupButton() {
        binding.button.setOnClickListener {
            viewModel.isRunning = !viewModel.isRunning
            binding.button.text =
                resources.getString(if (viewModel.isRunning) R.string.stop else R.string.start)
            startLocationService()
        }
    }

    private fun setupRecycler() {
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationService::class.java)
        intent.action = if (viewModel.isRunning) START else STOP
        ContextCompat.startForegroundService(this, intent)
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        if (!foregroundPermissionApproved()) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            binding.button.isEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> when (PackageManager.PERMISSION_GRANTED) {
                grantResults[0] -> {
                    binding.button.isEnabled = true
                    Log.d(TAG, "Location permission granted")
                }
                else -> {
                    binding.button.isEnabled = false
                    Log.d(TAG, "Location permission was not granted")
                    showPermissionDeniedExplanation()
                }
            }
        }
    }

    private fun showPermissionDeniedExplanation() {
        Snackbar.make(
            binding.root,
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.settings) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(
                    "package",
                    BuildConfig.APPLICATION_ID,
                    null
                )
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                // display the app settings screen
                startActivity(intent)
            }
            .show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
