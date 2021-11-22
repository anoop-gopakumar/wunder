package com.wundermobility.carrental.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.wundermobility.carrental.R
import com.wundermobility.carrental.databinding.ActivityMapsBinding
import com.wundermobility.carrental.models.Cars
import com.wundermobility.carrental.viewmodels.MapViewModels

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun fetchAllCars() {
        viewModels._carListLiveData.observe(this, Observer { cars ->

            cars.forEach {
                if(it.title == null || it.title!!.isEmpty()){
                    it.title ="N/A"
                }
            }

            markCarsGeolocations(cars)
        })
    }

    private fun markCarsGeolocations(cars: List<Cars>) {
        mMap?.let {

            tapCount=0
            clearMap()

            cars.forEach {
                val geoPosition = LatLng(it.lat.toDouble(), it.lon.toDouble())

                val markerOptions = MarkerOptions().position(geoPosition).title(it.title)
                    .snippet("Licence Plate: " + it.licencePlate)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))

                markerOptionsList.add(markerOptions)

                val inMarker :Marker? = mMap?.addMarker(markerOptions)
                inMarker?.tag =  it.carId

                mMap?.animateCamera(CameraUpdateFactory.newLatLng(geoPosition))
                mMap?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker ->

                    if(tapCount == 0){
                        lastClickedId = marker.tag as Int
                        clearAllMarkers(marker)
                    }else{

                        val intent = Intent(baseContext,CarDetailsActivity::class.java)
                        intent.putExtra("CarId", lastClickedId)
                        startActivity(intent )
                    }

                    tapCount++

                    return@OnMarkerClickListener false
                })
            }
        }
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

        val mOpt : MarkerOptions? =  clickedMarkerOptions.firstOrNull()

        if (mOpt != null) {
            val selectedMarker :Marker?= mMap?.addMarker(mOpt)
            selectedMarker?.tag= marker.tag
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(selectedMarker!!.position))
            selectedMarker?.showInfoWindow()
        }
    }


}