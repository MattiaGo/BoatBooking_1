package com.example.boatbooking_1.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.ActivityNotificationDetailsBinding
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.utils.Util

class NotificationBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailsBinding
//    private lateinit var booking: Booking

    private lateinit var rvServices: RecyclerView
    private lateinit var serviceList: ArrayList<BoatService>
    private lateinit var serviceAdapter: PublicServiceAdapter

    private var idBooking: String? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idBooking = intent.getStringExtra("id")
        serviceList = ArrayList()

        Log.d("MyService", "ID: $idBooking")

        rvServices = binding.rvServices

        serviceAdapter = PublicServiceAdapter(serviceList)
        binding.rvServices.adapter = serviceAdapter
        binding.rvServices.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .collection("Booking")
            .document(idBooking!!)
            .get()
            .addOnSuccessListener {
                val booking = it.toObject(Booking::class.java)

                Log.d("MyService", "Booking: ${booking.toString()}")

                binding.boatName.text = booking?.announcement!!.announce_name.toString()
                binding.tvLocation.text = booking.announcement!!.location.toString()
                binding.tvPassengers.text = booking.announcement!!.boat!!.passengers.toString()
                binding.tvBeds.text = booking.announcement!!.boat!!.beds.toString()
                binding.tvBathrooms.text = booking.announcement!!.boat!!.bathrooms.toString()
                binding.tvBuilder.text = booking.announcement!!.boat!!.builder.toString()
                binding.tvModel.text = booking.announcement!!.boat!!.model.toString()
                binding.tvYear.text = booking.announcement!!.boat!!.year.toString()
                binding.tvLength.text = booking.announcement!!.boat!!.length.toString()
                binding.tvCabins.text = booking.announcement!!.boat!!.cabins.toString()
                binding.tvDescription.text = booking.announcement!!.description.toString()
                binding.tvPrice.text = booking.total.toString()
                binding.tvStartDate.text = Util.sdfBooking().format(booking.startDate!!)
                binding.tvEndDate.text = Util.sdfBooking().format(booking.endDate!!)

                serviceList.addAll(booking.services!!)
                Log.d("MyService", serviceList.toString())
                serviceAdapter.notifyDataSetChanged()
            }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}
