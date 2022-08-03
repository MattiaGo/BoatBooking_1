package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentHomeBinding
import com.example.boatbooking_1.databinding.FragmentMyAnnouncementsBinding
import com.example.boatbooking_1.model.Announcement
import com.bumptech.glide.Glide
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.HomeAnnouncementViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var bestAnnouncementAdapter: PublicAnnouncementAdapter
    private lateinit var lastSeenAnnouncementAdapter: PublicAnnouncementAdapter
    private lateinit var mostRequestedLocationAdapter: PublicAnnouncementAdapter

    private lateinit var bestAnnouncementList: ArrayList<Announcement>
    private lateinit var lastSeenAnnouncementList: ArrayList<Announcement>
    private lateinit var mostRequestedLocationList: ArrayList<Announcement>

    private lateinit var rvBestCharters: RecyclerView
    private lateinit var rvRecentSeen: RecyclerView
    private lateinit var rvMostReqLocation: RecyclerView

    private val homeAnnouncementViewModel: HomeAnnouncementViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bestAnnouncementList = ArrayList()
        lastSeenAnnouncementList = ArrayList()
        mostRequestedLocationList = ArrayList()

        bestAnnouncementAdapter = PublicAnnouncementAdapter(bestAnnouncementList)
        lastSeenAnnouncementAdapter = PublicAnnouncementAdapter(lastSeenAnnouncementList)
        mostRequestedLocationAdapter = PublicAnnouncementAdapter(mostRequestedLocationList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //BEST CHARTER
        rvBestCharters = binding.rvBestCharters
        rvBestCharters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvBestCharters.adapter = bestAnnouncementAdapter

        //LAST SEEN
        rvRecentSeen = binding.rvRecentSeen
        rvRecentSeen.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvRecentSeen.adapter = bestAnnouncementAdapter

        //MOST REQ LOCATION
        rvMostReqLocation = binding.rvMostReqLocation
        rvMostReqLocation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvMostReqLocation.adapter = bestAnnouncementAdapter

//        getMyAnnouncements() // Realtime Database
//        homeAnnouncementViewModel.getBestHomeAnnouncements(bestAnnouncementList, bestAnnouncementAdapter)
        homeAnnouncementViewModel.getPositionAnnouncement(
            mostRequestedLocationList,
            MyAnnouncementAdapter(arrayListOf(), requireContext())
        )

//        Log.d("Firestore", mostRequestedLocationList.toString())

        //getBestHomeAnnouncements(bestAnnouncementList)
        binding = FragmentHomeBinding.inflate(inflater, container, false)


//        Glide.with(context!!)
//            .load("https://firebasestorage.googleapis.com/v0/b/boatbooking-9150d.appspot.com/o/images%2F2022-08-02%2014-00-28_0?alt=media&token=72b9610d-f29b-410f-8779-17ed29407336")
//            .into(binding.iv_home)

        // Test
//        storeFakeDataToDatabase()


        Log.d("myBestAnnouncements", bestAnnouncementList.toString())

        return binding.root
    }

    /*
    private fun storeFakeDataToDatabase() {
        Util.mDatabase.child("chats").child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .child("LD3qWBHBFmWVN87S1r9reBgGx6H3")
            .setValue(
                ChatPreview(
                    user = User(
                        "Matteo",
                        "",
                        "LD3qWBHBFmWVN87S1r9reBgGx6H3",
                        "Brescia",
                        "",
                        false
                    ), "Ciao!", timestamp = Timestamp.now().seconds
                )
            )

        Util.mDatabase.child("chats").child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .child("5bovw1afTudSfM1KPbUTANvbFxw2")
            .setValue(
                ChatPreview(
                    user = User(
                        "Test",
                        "",
                        "5bovw1afTudSfM1KPbUTANvbFxw2",
                        "Brescia",
                        "",
                        false
                    ), "Hola!", timestamp = Timestamp.now().seconds
                )
            )

        Util.mDatabase.child("chats").child("LD3qWBHBFmWVN87S1r9reBgGx6H3")
            .child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .setValue(
                ChatPreview(
                    user = User(
                        "Admin",
                        "",
                        "2PH1sbnjT4e1RkI0TYD6X1e1nD72",
                        "Brescia",
                        "",
                        false
                    ), "Ciao!", timestamp = Timestamp.now().seconds
                )
            )

        Util.mDatabase.child("users").child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .setValue(
                User(
                    "Admin",
                    "admin@ciao.com",
                    "2PH1sbnjT4e1RkI0TYD6X1e1nD72",
                    "Brescia",
                    "",
                    true
                )
            )

        Util.mDatabase.child("users").child("LD3qWBHBFmWVN87S1r9reBgGx6H3")
            .setValue(
                User(
                    "Matteo",
                    "ciao@mail.org",
                    "LD3qWBHBFmWVN87S1r9reBgGx6H3",
                    "Brescia",
                    "",
                    false
                )
            )
    }

     */

}