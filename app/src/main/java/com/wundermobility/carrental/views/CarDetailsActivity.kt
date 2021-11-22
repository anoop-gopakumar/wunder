package com.wundermobility.carrental.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.wundermobility.carrental.R
import com.wundermobility.carrental.databinding.ActivityCarDetailsBinding
import com.wundermobility.carrental.models.CarInfo
import com.wundermobility.carrental.utils.ImageUtils
import com.wundermobility.carrental.viewmodels.DetailsViewModel
import com.wundermobility.carrental.views.adapter.DetailsInfoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CarDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarDetailsBinding

    private val viewModels: DetailsViewModel by viewModels()

    private var carId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carId = intent.getIntExtra("CarId", -1)

        observeData()
        fetchCarInfo()
        initButtonClickListener()
    }

    private fun fetchCarInfo() {

        CoroutineScope(IO).launch {
            viewModels.fetchCarDetails(carId)
        }
    }

    private fun initButtonClickListener() {

        binding.quickRent.setOnClickListener {

            binding.progress.visibility = View.VISIBLE
            CoroutineScope(IO).launch {
                viewModels.bookRental(carId)
            }
        }
    }

    private fun observeData() {

        viewModels.carDetailsLiveData.observe(this, Observer {

            if (it != null) {
                showUI(it)
            } else {
                Snackbar.make(
                    binding.root,
                    " The selected vehicle is not available at the moment please select another one ",
                    Snackbar.LENGTH_LONG
                ).show()
                Handler(Looper.getMainLooper()).postDelayed( {
                    finish()
                },2000)
            }

        })

        viewModels.rentalInfoLiveData.observe(this, Observer {

            binding.progress.visibility = View.GONE
            binding.quickRent.isEnabled = false
            if (it != null) {
                Snackbar.make(
                    binding.root,
                    " The vehicle has successfully booked ",
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                Snackbar.make(
                    binding.root,
                    " unable to book the vehicle ",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        })
    }


    private fun showUI(carInfo: CarInfo) {

        ImageUtils.loadImage(baseContext, carInfo.vehicleTypeImageUrl, binding.imageView)

        val data = ArrayList<Pair<String, String>>()

        data.add(Pair("Name", carInfo.title))
        data.add(Pair("Licence", carInfo.licencePlate))
        data.add(Pair("Address", carInfo.address))
        data.add(Pair("Zipcode", carInfo.zipCode))
        data.add(Pair("Reservation State", carInfo.reservationState.toString()))
        data.add(Pair("Fuel Level", carInfo.fuelLevel.toString()))

        val adapter = DetailsInfoAdapter(data)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

    }

}