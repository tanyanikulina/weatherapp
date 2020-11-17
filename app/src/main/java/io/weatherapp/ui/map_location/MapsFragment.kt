package io.weatherapp.ui.map_location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.weatherapp.R
import java.util.*

class MapsFragment : Fragment() {

    lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var myLocationMarker: MarkerOptions

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        googleMap.setOnMapClickListener {

            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            val cityName = findCityName(it.latitude, it.longitude)

            googleMap.animateCamera(
                CameraUpdateFactory.newLatLng(it),
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() {
                        goBackWithParams(cityName, it.latitude, it.longitude)
                    }

                    override fun onCancel() {
                        /**do nothing*/
                    }
                })
        }

        if (!::myLocationMarker.isInitialized)
            findLocation {
                addMarkerForLocation(googleMap, it.latitude, it.longitude)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        requestLocationPermissions()
    }

    fun requestLocationPermissions() {
        val PERMISSION_LOCATION = 454545
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_LOCATION
            )
        }
    }

    fun findLocation(onFound: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), getString(R.string.no_location), Toast.LENGTH_SHORT)
                .show()
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener(requireActivity()) { location: Location? ->
                    location?.let {
                        onFound(location)
                    }
                }
        }
    }

    fun goBackWithParams(cityName: String, lat: Double, lon: Double) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "location", Triple(cityName, lat.toString(), lon.toString())
        )
        findNavController().popBackStack()
    }

    fun findCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            return addresses[0].getAddressLine(0) ?: ""
        }
        return ""
    }

    fun addMarkerForLocation(map: GoogleMap, lat: Double, lon: Double) {
        val myLocation = LatLng(lat, lon)
        myLocationMarker = MarkerOptions().position(myLocation).title("Marker in my location")
        map.addMarker(myLocationMarker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (::mMap.isInitialized) {
            findLocation {
                addMarkerForLocation(mMap, it.latitude, it.longitude)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}