package com.threedee.nature.add

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxrelay2.PublishRelay
import com.threedee.nature.BuildConfig
import com.threedee.nature.R
import com.threedee.nature.databinding.ActivityPickFarmCoordinatesBinding
import com.threedee.nature.eventBus.MessageEvent
import com.threedee.nature.eventBus.RxBus
import com.threedee.nature.util.showSnackbar
import com.threedee.presentation.viewmodel.FarmViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.DateFormat
import java.util.Date
import javax.inject.Inject

class PickFarmCoordinatesActivity : DaggerAppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    //these variables control the moving of the car on the map
    private var marker: Marker? = null
    private var pickHereMarker: Marker? = null
    private var v: Float = 0F
    private var mMap: GoogleMap? = null
    private var emission = 0
    private val autoCompletePublishSubject = PublishRelay.create<LatLng>()
    private var latLngDisposable: Disposable? = null
    /**
     * Provides access to the Fused Location Provider API.
     */
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    /**
     * Provides access to the Location Settings API.
     */
    lateinit var mSettingsClient: SettingsClient
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    lateinit var mLocationRequest: LocationRequest
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    lateinit var mLocationSettingsRequest: LocationSettingsRequest
    /**
     * Callback for Location events.
     */
    lateinit var mLocationCallback: LocationCallback
    /**
     * Represents a geographical location.
     */
    var mCurrentLocation: Location? = null
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private var mRequestingLocationUpdates: Boolean = false
    /**
     * Time when the location was updated represented as a String.
     */
    lateinit var mLastUpdateTime: String
    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private var addressRequested = false // this variable was used to track the progress bar
    var polyline: Polyline? = null
    var locationsArray = arrayListOf<Location>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var farmViewModel: FarmViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var binding: ActivityPickFarmCoordinatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_farm_coordinates)
        initViews()
        initViewModel()

        initLocation(savedInstanceState)
    }

    private fun initViewModel() {
        farmViewModel = ViewModelProvider(this, viewModelFactory).get(FarmViewModel::class.java)
        if (farmViewModel.locations.value != null) {
            var latLngs = arrayListOf<LatLng>()
            locationsArray = farmViewModel.locations.value!!
            farmViewModel.locations.value?.forEachIndexed { index, location ->
                val latLng = LatLng(location.latitude, location.longitude)
                latLngs.add(latLng)
            }
            if(latLngs.size > 2) latLngs.add(LatLng(farmViewModel.locations.value!![0].latitude, farmViewModel.locations.value!![0].longitude))
            latLngs.forEach { addMarker(it) }
            drawPolyline(latLngs)
        }
    }

    private fun initViews() {
        initBottomSheet()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.bottomSheet.closeButton.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.bottomSheet.closeSheet.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.pickPointButton.setOnClickListener {
            if (mCurrentLocation != null) {
                addMarker(LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude))
                var latLngs = arrayListOf<LatLng>()
                locationsArray.add(mCurrentLocation!!)
                farmViewModel.locations.value = locationsArray
                farmViewModel.locations.value?.forEachIndexed { index, location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    latLngs.add(latLng)
                }
                // close the polyline loop
                if(latLngs.size > 2) latLngs.add(LatLng(farmViewModel.locations.value!![0].latitude, farmViewModel.locations.value!![0].longitude))
                drawPolyline(latLngs)
            }
        }
        binding.doneButton.setOnClickListener {
            var latLngs = arrayListOf<LatLng>()
            farmViewModel.locations.value?.forEachIndexed { index, location ->
                val latLng = LatLng(location.latitude, location.longitude)
                latLngs.add(latLng)
            }
            // close the polyline loop
            if(latLngs.size > 2) latLngs.add(LatLng(farmViewModel.locations.value!![0].latitude, farmViewModel.locations.value!![0].longitude))
            if (farmViewModel.locations.value != null) RxBus.publish(MessageEvent(farmViewModel.locations.value!!))
            finish()
        }
        binding.startAgainButton.setOnClickListener {
            mMap?.clear()
            farmViewModel.locations.value = arrayListOf()
            locationsArray = arrayListOf()
        }
    }

    private fun initBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet)
        sheetBehavior.bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        }
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun addMarker(latLng: LatLng) {
        if (mMap == null) return
        mMap!!.addMarker(
            MarkerOptions().position(latLng)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_place_marker))
        )
    }

    private fun drawPolyline(latLngs: List<LatLng>) {
        if (latLngs.size < 2) return
        if (mMap == null) return
        polyline?.remove()
        Timber.e("drawing polyline")
        val POLYLINE_STROKE_WIDTH_PX = 12F
        polyline = mMap!!.addPolyline(PolylineOptions()
            .addAll(latLngs.toMutableList()))
        polyline?.endCap = RoundCap()
        polyline?.width = POLYLINE_STROKE_WIDTH_PX
        polyline?.color = Color.BLACK
        polyline?.jointType = JointType.ROUND
    }

    /**
     * Take the emissions from the Rx Relay as a pair of LatLng and starts the animation of
     * car on map by taking the 2 pair of LatLng's.
     *
     * @param latLngs List of LatLng emitted by Rx Relay with size two.
     */
    private fun animatePersonOnMap(latLngs: List<LatLng>) {
        if (mMap == null) return
        val builder = LatLngBounds.Builder()
        for (latLng in latLngs) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        val mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2)
        mMap!!.animateCamera(mCameraUpdate)
        if (emission == 1 || marker == null) {
            marker = mMap!!.addMarker(
                MarkerOptions().position(latLngs[0])
                    .flat(true)
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_navigation))
            )
        }
        marker!!.position = latLngs[0]
        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            v = valueAnimator.animatedFraction
            val lng = v * latLngs[1].longitude + (1 - v) * latLngs[0].longitude
            val lat = v * latLngs[1].latitude + (1 - v) * latLngs[0].latitude
            val newPos = LatLng(lat, lng)
            marker!!.position = newPos
            marker!!.setAnchor(0.5f, 0.5f)
            marker!!.rotation = getBearing(latLngs[0], newPos)
            var zoom = mMap!!.cameraPosition.zoom
            if (zoom <= 15) {
                zoom = 15.5f
            }
            mMap!!.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(newPos)
                        .zoom(zoom).build()
                )
            )
        }
        valueAnimator.start()
    }

    /**
     * Bearing between two LatLng pair
     *
     * @param begin First LatLng Pair
     * @param end Second LatLng Pair
     * @return The bearing or the angle at which the marker should rotate for going to `end` LAtLng.
     */
    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return -1f
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable?.intrinsicWidth!!,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun initLocation(savedInstanceState: Bundle?) {
        mRequestingLocationUpdates = false
        mLastUpdateTime = ""
        // Update values using data stored in the Bundle.
        updateLocationValuesFromBundle(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private fun updateLocationValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    KEY_REQUESTING_LOCATION_UPDATES
                )
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation =
                    savedInstanceState.getParcelable(KEY_LOCATION) //todo:update the map from here - why?
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime =
                    savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING).toString()
            }
            updateUI()
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())

                updateLocationUI()
            }
        }
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Timber.e("User agreed to make required location settings changes.")
                    }
                    Activity.RESULT_CANCELED -> {
                        Timber.e("User chose not to make required location settings changes.")
                        mRequestingLocationUpdates = false
                        updateUI()
                    }
                }
            }
        }
    }

    /**
     * Handles the Start Updates and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    private fun startUpdatesHandler() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true
            startLocationUpdates()
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener {
                Timber.e("All location settings are satisfied.")

                //noinspection MissingPermission
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )

                updateUI()

            }
            .addOnFailureListener {
                val statusCode = (it as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = it as ResolvableApiException
                            rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                            Timber.e("PendingIntent unable to execute request.")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " +
                            "fixed here. Fix in Settings."
                        Timber.e(errorMessage)
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        mRequestingLocationUpdates = false
                    }

                }
                updateUI()
            }
    }

    /**
     * Handles the Stop Updates, and requests removal of location updates.
     */
    private fun stopUpdatesHandler() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        stopLocationUpdates()
    }

    /**
     * Updates all UI fields.
     */
    private fun updateUI() {
        updateLocationUI()
    }

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            Timber.e("lat: ${mCurrentLocation!!.latitude} lng: ${mCurrentLocation!!.longitude}")
            animateCamera(LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude))
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Timber.e("stopLocationUpdates: updates never requested, no-op.")
            return
        }
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener {
                mRequestingLocationUpdates = false
            }
    }

    public override fun onResume() {
        super.onResume()
        beginLocationUpdates()

        latLngDisposable = autoCompletePublishSubject
            .buffer(2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                emission++
                animatePersonOnMap(result)
            }, { t: Throwable? -> Timber.w(t, "Failed to animate car on map") })
    }

    private fun beginLocationUpdates() {
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates()
        } else if (!checkPermissions()) {
            requestPermissions()
        }
        startUpdatesHandler()
        updateUI()
    }

    override fun onPause() {
        super.onPause()

        // Remove location updates to save battery.
        stopLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdatesHandler()
        if (latLngDisposable != null && !latLngDisposable?.isDisposed!!)
            latLngDisposable?.dispose()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState ?: return

        with(savedInstanceState) {
            // Save whether the address has been requested.
            putBoolean(ADDRESS_REQUESTED_KEY, addressRequested)

            putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates)
            putParcelable(KEY_LOCATION, mCurrentLocation)
            putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime)
        }
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState ?: return
        super.onRestoreInstanceState(savedInstanceState)
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showPermissionSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {
        showSnackbar(
            getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE,
            getString(actionStringId),
            listener
        )
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.e("Displaying permission rationale to provide additional context.")
            showPermissionSnackbar(R.string.permission_rationale,
                android.R.string.ok, View.OnClickListener {
                    ActivityCompat.requestPermissions(
                        applicationContext as Activity,
                        Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                })
        } else {
            Timber.e("Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                this, Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Timber.e("onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Timber.e("User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Timber.e("Permission granted, updates requested, starting location updates")
                    startLocationUpdates()
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showPermissionSnackbar(R.string.permission_denied_explanation,
                    R.string.settings, View.OnClickListener {
                        val intent = Intent()
                        intent.apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID, null
                            )
                            data = uri
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    })
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.setMinZoomPreference(12.0f)
        mMap!!.uiSettings.isCompassEnabled = false
        mMap!!.uiSettings.isMapToolbarEnabled = false
        mMap!!.uiSettings.isTiltGesturesEnabled = false
        if (checkPermissions()) mMap!!.isMyLocationEnabled = true
    }

    override fun onCameraIdle() {
        //protect when from on saved instance state and map is null
        if (mMap == null) return
        val latLng = mMap!!.cameraPosition.target
    }

    private fun animateCamera(latLng: LatLng) {
        if (mMap == null) return
        var zoom = mMap!!.cameraPosition.zoom
        if (zoom <= 12) {
            zoom = 15f
        }
        mMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                ), zoom
            )
        )

        autoCompletePublishSubject.accept(latLng)
    }

    companion object {
        /**
         * Code used in requesting runtime permissions.
         */
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private val REQUEST_CALL_PERMISSIONS_REQUEST_CODE = 35

        /**
         * Constant used in the location settings dialog.
         */
        private val REQUEST_CHECK_SETTINGS = 0x1

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 30 * 1000

        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        private val ADDRESS_REQUESTED_KEY = "address-request-pending"

        // Keys for storing activity state in the Bundle.
        private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
        private val KEY_LOCATION = "location"
        private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PickFarmCoordinatesActivity::class.java))
        }
    }
}