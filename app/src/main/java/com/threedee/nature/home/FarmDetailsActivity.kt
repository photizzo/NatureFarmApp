package com.threedee.nature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityFarmDetailsBinding

class FarmDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityFarmDetailsBinding
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_farm_details)
        initViews()
    }

    private fun initViews() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.farmMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, FarmDetailsActivity::class.java))
        }
    }
}
