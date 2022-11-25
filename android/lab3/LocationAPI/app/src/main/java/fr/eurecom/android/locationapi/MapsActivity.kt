package fr.eurecom.android.locationapi

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import fr.eurecom.android.locationapi.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var provider: String
    lateinit var location: Location
    lateinit var latitudeField: TextView
    lateinit var longitudeField: TextView
    private val MY_PERMISSIONS_LOCATION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Start the location manager
//        val criteria = Criteria()
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        provider = locationManager.getBestProvider(criteria, false)!!
//        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.i("Permission: ", "To be checked")
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
//                MY_PERMISSIONS_LOCATION
//            )
//        } else Log.i("Permission: ", "Granted")
//
//        provider = locationManager.getBestProvider(criteria, false)!!
//        location = locationManager.getLastKnownLocation(provider)!!
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }

    override fun onStart() {
        super.onStart()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled: Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            Log.i("GPS", "not enabled")
            enableLocationSettings()
        } else {
            Log.i("GPS", "enabled")
        }
    }

    private fun enableLocationSettings(){
        val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingsIntent)
    }

    override fun onPause(){
        super.onPause()
        locationManager.removeUpdates(this)
    }

    fun showLocation(view: View) {
        when (view.id) {
            R.id.Button01 -> {
                updateLocationView()
            }
        }
    }
    private fun updateLocationView() {
        if (location != null){
            val lat: Double = location.latitude
            val lng: Double = location.longitude
            latitudeField.text = lat.toString()
            longitudeField.text = lng.toString()
        } else {
            Log.i("showLocation", "NULL")
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.i("Location", "LOCATION CHANGED!!!")
        updateLocationView()
    }
    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this, "Enabled new provider $provider", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this, "Disabled provider $provider", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Access:", "Now permissions are granted")
                } else {
                    Log.i("Access:", " permissions are denied")
                }
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}