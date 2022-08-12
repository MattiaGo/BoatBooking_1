package com.example.boatbooking_1.ui

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.ActivityMainBinding
import com.example.boatbooking_1.notification.AlarmReceiver
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    // View Binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val intervalMillis: Long = 60000 * 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startAlarm()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val node = navController.graph.findNode(R.id.account)
            (node as NavGraph).setStartDestination(R.id.userProfile)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.passwordResetFragment -> hideBottomNav()
                R.id.account -> hideBottomNav()
                R.id.chatFragment -> hideBottomNav()
                R.id.chatFragmentFromAnnouncement -> hideBottomNav()
                R.id.registration -> hideBottomNav()
                R.id.addBoatFragment -> hideBottomNav()
                R.id.addAnnouncementFragment -> hideBottomNav()
                R.id.announcementDetailsFragment -> hideBottomNav()
                R.id.editAnnouncementFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }

    private fun startAlarm() {
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(applicationContext, AlarmReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            intervalMillis,
            pendingIntent
        )
    }


    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }
}