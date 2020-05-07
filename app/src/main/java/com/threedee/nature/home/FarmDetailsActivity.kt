package com.threedee.nature.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityFarmDetailsBinding
import kotlinx.android.synthetic.main.dashboard_info.view.*

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

    private fun callPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    private fun setProfileImage(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .fallback(R.drawable.ic_person)
            .into(binding.profileImageView)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, FarmDetailsActivity::class.java))
        }
    }
}
