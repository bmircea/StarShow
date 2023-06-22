package com.example.starshow

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.starshow.databinding.ActivityAddObservationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AddObservationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddObservationBinding
    private lateinit var buttonLocation : Button
    private lateinit var title : String
    private lateinit var description : String
    private lateinit var location : Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddObservationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        buttonLocation = findViewById(R.id.buttonPickLocation)

        buttonLocation.setOnClickListener{
            // Get Location
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc : Location ->
                    location = loc
                }
        }

    }


}