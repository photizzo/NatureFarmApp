package com.threedee.nature.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.threedee.cache.db.Converters
import com.threedee.domain.model.Farm
import com.threedee.nature.R
import com.threedee.nature.add.AddFarmActivity
import com.threedee.nature.databinding.ActivityFarmDetailsBinding
import com.threedee.nature.util.showSnackbar
import com.threedee.presentation.state.Resource
import com.threedee.presentation.state.ResourceState
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.dashboard_info.view.*
import timber.log.Timber
import javax.inject.Inject

class FarmDetailsActivity : DaggerAppCompatActivity(), OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var farmViewModel: FarmViewModel
    private lateinit var binding: ActivityFarmDetailsBinding
    private var mMap: GoogleMap? = null
    var polyline: Polyline? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_farm_details)
        initViewModel()
        initViews()
    }

    private fun initViews() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.farmMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val farm = getFarmFromString(intent.getStringExtra("data")?: return)
        binding.farmNameTextView.text = farm.farmer.fullName.substringAfter(" ")
        binding.firstNameTextView.text = farm.farmer.fullName.substringBefore(" ")
        binding.addressTextView.text = farm.farmLocation.address
        binding.emailtextView.text = farm.farmer.email
        binding.farmNameTextView.text = farm.farmLocation.name

        setProfileImage(farm.farmer.avatar)
        var latLngs = arrayListOf<LatLng>()
        farm.farmLocation.latitude.forEachIndexed { index, _ ->
            val latLng = LatLng(farm.farmLocation.latitude[index], farm.farmLocation.longitude[index])
            latLngs.add(latLng)
        }
        drawPolyline(latLngs)
        binding.callButton.setOnClickListener {
            callPhone(farm.farmer.phone)
        }
        binding.editButton.setOnClickListener {
            AddFarmActivity.startActivity(this, convertFarmToString(farm))
        }
        binding.deleteButton.setOnClickListener {
            farmViewModel.deleteFarm(farm)
        }
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() {
        farmViewModel = ViewModelProvider(this, viewModelFactory).get(FarmViewModel::class.java)
        farmViewModel.deleteFarmLiveData.observe(this, Observer {
            handleDeleteFarm(it)
        })
    }

    private fun handleDeleteFarm(resource: Resource<Unit>) {
        when (resource.status) {
            ResourceState.LOADING -> {
            }
            ResourceState.SUCCESS -> {
                finish()
            }
            ResourceState.ERROR -> {
                resource.message?.let {
                    showSnackbar(it) }
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap!!.setMinZoomPreference(12.0f)
        mMap!!.uiSettings.isCompassEnabled = false
        mMap!!.uiSettings.isMapToolbarEnabled = false
        mMap!!.uiSettings.isTiltGesturesEnabled = false
    }

    private fun drawPolyline(latLngs: List<LatLng>) {
        if (latLngs.size < 2) return
        if (mMap == null) return
        polyline?.remove()
        Timber.e("drawing polyline")
        val POLYLINE_STROKE_WIDTH_PX = 12F
        polyline = mMap!!.addPolyline(
            PolylineOptions()
                .addAll(latLngs.toMutableList()))
        polyline?.endCap = RoundCap()
        polyline?.width = POLYLINE_STROKE_WIDTH_PX
        polyline?.color = Color.BLACK
        polyline?.jointType = JointType.ROUND
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
        fun getFarmFromString(data: String): Farm {
            val listType = object : TypeToken<Farm>(){}.type
            return Gson().fromJson(data, listType)
        }

        fun convertFarmToString(farm: Farm): String {
            return Gson().toJson(farm)
        }

        fun startActivity(context: Context, data: String) {
            context.startActivity(Intent(context, FarmDetailsActivity::class.java).apply {
                putExtra("data", data)
            })
        }
    }
}
