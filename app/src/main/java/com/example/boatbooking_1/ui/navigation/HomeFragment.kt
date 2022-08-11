package com.example.boatbooking_1.ui.navigation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentHomeBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.MainActivity
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.viewmodel.HomeAnnouncementViewModel
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var fAuth: FirebaseAuth

    private val homeAnnouncementViewModel: HomeAnnouncementViewModel by activityViewModels()
    private val userViewModel: UserProfileViewModel by activityViewModels()

    private lateinit var lastAddedAdapter: PublicAnnouncementAdapter
    private lateinit var lastSeenAdapter: PublicAnnouncementAdapter
    private lateinit var mostRequestedAdapter: PublicAnnouncementAdapter

    private lateinit var lastAddedList: ArrayList<Announcement>
    private lateinit var lastSeenList: ArrayList<Announcement>
    private lateinit var mostRequestedList: ArrayList<Announcement>

    private lateinit var rvLastAdded: RecyclerView
    private lateinit var rvRecentSeen: RecyclerView
    private lateinit var rvMostRequested: RecyclerView

    private var remoteImagesURILastAddedList: MutableList<String> = mutableListOf()
    private var remoteImagesURILastSeenList: MutableList<String> = mutableListOf()
    private var remoteImagesURIMostRequestedList: MutableList<String> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        fAuth = FirebaseAuth.getInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lastAddedList = ArrayList()
        lastSeenList = ArrayList()
        mostRequestedList = ArrayList()

        lastAddedAdapter =
            PublicAnnouncementAdapter(lastAddedList, requireContext(), remoteImagesURILastAddedList)
        lastSeenAdapter =
            PublicAnnouncementAdapter(lastSeenList, requireContext(), remoteImagesURILastSeenList)
        mostRequestedAdapter = PublicAnnouncementAdapter(
            mostRequestedList,
            requireContext(),
            remoteImagesURIMostRequestedList
        )
//        lastAddedAdapter = PublicAnnouncementAdapter(lastAddedList, requireContext(), ArrayList())
//        lastSeenAdapter = PublicAnnouncementAdapter(lastSeenList, requireContext(), ArrayList())

        userViewModel.getUser()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lastAddedList.clear()
        lastSeenList.clear()
        mostRequestedList.clear()

        remoteImagesURILastAddedList.clear()
        remoteImagesURIMostRequestedList.clear()
        remoteImagesURILastSeenList.clear()

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //LAST ADDED
        rvLastAdded = binding.rvLastAdded
        rvLastAdded.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvLastAdded.adapter = lastAddedAdapter
        binding.layoutLogo.setOnClickListener {
            showNotification()
        }
        //BEST CHARTER
//        rvLastAdded = binding.rvLastAdded
//        rvLastAdded.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        rvLastAdded.adapter = lastAddedAdapter

        //LAST SEEN
        rvRecentSeen = binding.rvRecentSeen
        rvRecentSeen.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvRecentSeen.adapter = lastSeenAdapter

        //MOST REQ LOCATION
        rvMostRequested = binding.rvMostRequested
        rvMostRequested.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvMostRequested.adapter = mostRequestedAdapter



        homeAnnouncementViewModel.getLastAddedAnnouncement(
            lastAddedList,
            lastAddedAdapter,
            remoteImagesURILastAddedList
        )

        homeAnnouncementViewModel.getMostRequestedAnnouncement(
            mostRequestedList,
            mostRequestedAdapter,
            remoteImagesURIMostRequestedList
        )

        if (fAuth.currentUser !== null) {
            binding.layoutLastSeen.isVisible = true
            homeAnnouncementViewModel.getLastSeenAnnouncement(
                lastSeenList,
                lastSeenAdapter,
                remoteImagesURILastSeenList
            )
        }

        return binding.root
    }

    private fun showNotification() {
        var notificationManager: NotificationManager? = null
        val ID = 0
        val name = "Test1"
        val id = "test1"
        val description = "test_description1"

        val pendingIntent: PendingIntent

        if (notificationManager == null) {
            notificationManager =
                activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notificationManager.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, name, importance)
                mChannel.description = description
                mChannel.enableVibration(true)
                mChannel.lightColor = Color.GREEN
//                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notificationManager.createNotificationChannel(mChannel)
            }

        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(activity!!, id)

        val intent: Intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.action = "DEBUG"
        intent.putExtra("ciao", "IntentTest")
        pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        builder.setContentTitle("My Notification")
            .setSmallIcon(R.drawable.ic_rudder)
            .setContentText("KotlinApp")
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setTicker("Notification")
            .setVibrate(longArrayOf())

        val dismissIntent = Intent(activity, MainActivity::class.java)
        dismissIntent.action = "TEST"
        dismissIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        dismissIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        dismissIntent.putExtra("id", "_DEBUG")

        val pendingDismissIntent =
            PendingIntent.getActivity(
                activity,
                System.currentTimeMillis().toInt(),
                dismissIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        val dismissAction = NotificationCompat.Action(null, "EXAMPLE", pendingDismissIntent)

        builder.addAction(dismissAction)
        val notification = builder.build()

        notificationManager.notify(ID, notification)

    }

}