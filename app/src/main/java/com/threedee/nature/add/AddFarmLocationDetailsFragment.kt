package com.threedee.nature.add

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.threedee.domain.model.Farm
import com.threedee.domain.model.FarmLocation
import com.threedee.nature.R
import com.threedee.nature.databinding.LayoutFarmDetailsBinding
import com.threedee.nature.eventBus.MessageEvent
import com.threedee.nature.eventBus.RxBus
import com.threedee.nature.util.isValidName
import com.threedee.nature.util.showSnackbar
import com.threedee.nature.util.validate
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class AddFarmLocationDetailsFragment : DaggerFragment(), OnMapReadyCallback {
    private lateinit var binding: LayoutFarmDetailsBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var farmViewModel: FarmViewModel
    private var mMap: GoogleMap? = null
    var polyline: Polyline? = null
    var latLngCache = arrayListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.layout_farm_details, container, false
        )
        initViewModel()
        activity?.let {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.pickCoordinatorButton.setOnClickListener {
            PickFarmCoordinatesActivity.startActivity(view.context)
        }
        binding.previewButton.setOnClickListener {
            farmViewModel.currentPage.value = 0
        }
        binding.doneButton.setOnClickListener {
            if (validateUserInput()) {
                val farmer = farmViewModel.farmer.value
                val farmLocation =
                    FarmLocation(-1, binding.farmNameTextField.editText?.text.toString(),
                        binding.addressTextField.editText?.text.toString(),
                        farmViewModel.locations.value?.map { it.latitude } ?: arrayListOf(),
                        farmViewModel.locations.value?.map { it.longitude } ?: arrayListOf())
                if (farmer == null) {
                    activity?.showSnackbar("Please refill farmer details!")
                    return@setOnClickListener
                }
                if (farmViewModel.isUpdating.value == true) {
                    farmViewModel.updateFarm(
                        Farm(
                            farmViewModel.updateId.value ?: 0,
                            farmer,
                            farmLocation
                        )
                    )
                } else {
                    farmViewModel.addFarm(Farm(0, farmer, farmLocation))
                }
            } else {
                activity?.showSnackbar("Fields marked * are compulsory")
            }
        }
        binding.backButton.setOnClickListener {
            activity?.finish()
        }
    }

    @SuppressLint("CheckResult")
    private fun initViewModel() {
        activity?.let {activity
            farmViewModel = ViewModelProvider(it, viewModelFactory).get(FarmViewModel::class.java)
            farmViewModel.farmLocation.observe(viewLifecycleOwner, Observer { farmLocation ->
                binding.farmNameTextField.editText?.setText(farmLocation.name)
                binding.addressTextField.editText?.setText(farmLocation.address)
                var latLngs = arrayListOf<Location>()
                farmLocation.latitude.forEachIndexed { index, _ ->
                    val location = Location("")
                    location.latitude = farmLocation.latitude[index]
                    location.longitude = farmLocation.longitude[index]
                    latLngs.add(location)
                }
                farmViewModel.locations.value = latLngs
            })
        }
        RxBus.listen(MessageEvent::class.java).subscribe { data ->
            var latLngs = arrayListOf<LatLng>()
            farmViewModel.locations.value = data.latLng
            farmViewModel.locations.value?.forEachIndexed { index, location ->
                val latLng = LatLng(location.latitude, location.longitude)
                latLngs.add(latLng)
            }
            latLngCache = latLngs
            drawPolyline(latLngs)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val myPlace = LatLng(40.73, -73.99)  // this is New York
        mMap!!.setMinZoomPreference(15.0f)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
        if (latLngCache.isNotEmpty()) drawPolyline(latLngCache)
    }

    private fun drawPolyline(latLngs: List<LatLng>) {
        if (latLngs.size < 2) return
        if (mMap == null) return
        polyline?.remove()
        Timber.e("drawing polyline")
        val POLYLINE_STROKE_WIDTH_PX = 12F
        polyline = mMap!!.addPolyline(
            PolylineOptions()
                .addAll(latLngs.toMutableList())
        )
        polyline?.endCap = RoundCap()
        polyline?.width = POLYLINE_STROKE_WIDTH_PX
        polyline?.color = Color.BLACK
        polyline?.jointType = JointType.ROUND
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLngs[0]))
    }

    private fun validateUserInput(): Boolean {
        binding.farmNameTextField.editText?.validate("Valid name required!") { data -> data.isValidName() }
        binding.addressTextField.editText?.validate("Valid address required!") { data -> data.isValidName() }

        return binding.farmNameTextField.editText?.text.toString().isValidName() && binding.addressTextField.editText?.text.toString().isValidName()
            && farmViewModel.locations.value != null
    }
}