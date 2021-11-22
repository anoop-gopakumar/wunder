package com.wundermobility.carrental.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.wundermobility.carrental.R
import com.wundermobility.carrental.databinding.ActivityMapsBinding
import com.wundermobility.carrental.models.Cars
import com.wundermobility.carrental.viewmodels.MapViewModels

class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleMap.OnMarkerClickListener{

    private var mMap: GoogleMap? = null
    private lateinit var binding: ActivityMapsBinding
    private val viewModels: MapViewModels by viewModels()
    private var markerOptionsList = ArrayList<MarkerOptions>()

    private var tapCount =0
    private var lastClickedId = -1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        fetchAllCars()
    }

    private fun markCarsGeolocations(cars: List<Cars>) {
        mMap?.let {

            tapCount=0
            clearMap()

            cars.forEach {

                val geoPosition = LatLng(it.lat.toDouble(), it.lon.toDouble())

                val markerOptions = MarkerOptions().position(geoPosition).title(it.title)
                    .snippet("Licence Plate: " + it.licencePlate)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cars))

                markerOptionsList.add(markerOptions)

                val inMarker :Marker? = mMap?.addMarker(markerOptions)
                inMarker?.tag =  it.carId

                mMap?.animateCamera(CameraUpdateFactory.newLatLng(geoPosition))
                mMap?.setOnMarkerClickListener(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        if(tapCount == 0){

            lastClickedId = marker.tag as Int
            clearAllMarkers(marker)
        }else{

            val intent = Intent(baseContext,CarDetailsActivity::class.java)
            intent.putExtra("CarId", lastClickedId)
            startActivity(intent )
        }

        tapCount++

        return false
    }

    private fun fetchAllCars() {
        viewModels.carListLiveData.observe(this, Observer { response ->

            if(response?.body() != null){
                response.body()?.forEach {
                    if(it.title == null || it.title!!.isEmpty()){
                        it.title ="N/A"
                    }
                }
                response.body()?.let {
                    markCarsGeolocations(it)
                }

            }else{
                Snackbar.make(
                    binding.root,
                    " Unable to fetch the car list ",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun clearMap(){

        mMap?.clear()
        markerOptionsList.clear()
    }

    private fun clearAllMarkers(marker :Marker){

        mMap?.clear()
        val clickedMarkerOptions = markerOptionsList.filter {
            it.title.equals(marker.title)
        }

        val markerOptions : MarkerOptions? =  clickedMarkerOptions.firstOrNull()

        if (markerOptions != null) {

            val selectedMarker :Marker?= mMap?.addMarker(markerOptions)
            selectedMarker?.let {
                it.tag= marker.tag
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(it.position))
                it.showInfoWindow()
            }
        }
    }

}