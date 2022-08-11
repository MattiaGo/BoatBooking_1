package com.example.boatbooking_1.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.boatbooking_1.databinding.ActivityMainBinding
import com.example.boatbooking_1.databinding.ActivityNotificationDetailsBinding
import com.example.boatbooking_1.model.Booking

class NotificationBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationDetailsBinding
    private var idBooking: String? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idBooking = intent?.getStringExtra("id")
        binding.boatName.text = idBooking

        if (intent?.action == "VIEW_BOOKING") {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.cancel(0)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idBooking = intent.getStringExtra("id")
        binding.boatName.text = idBooking

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}
