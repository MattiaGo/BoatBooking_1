package com.example.boatbooking_1.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.FragmentMyAnnouncementsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.auth.PasswordResetFragmentDirections
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

class MyAnnouncementsFragment : Fragment() {

    private lateinit var binding: FragmentMyAnnouncementsBinding

    private val announcementViewModel: AnnouncementViewModel by activityViewModels()

    private lateinit var myAnnouncementAdapter: MyAnnouncementAdapter
    private lateinit var myAnnouncementList: ArrayList<Announcement>

    private lateinit var rvMyAnnouncements: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myAnnouncementList = ArrayList()
        myAnnouncementAdapter = MyAnnouncementAdapter(myAnnouncementList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyAnnouncementsBinding.inflate(inflater, container, false)
        rvMyAnnouncements = binding.rvMyAnnouncements

        rvMyAnnouncements.layoutManager = LinearLayoutManager(this.context)
        rvMyAnnouncements.adapter = myAnnouncementAdapter

//        getMyAnnouncements() // Realtime Database
        announcementViewModel.getOwnerAnnouncement(myAnnouncementList,myAnnouncementAdapter)
        //getMyAnnouncementsFirestore()

        Log.d("myAnnouncements", myAnnouncementList.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            val action = MyAnnouncementsFragmentDirections.actionMyAnnouncementsFragmentToUserProfile()
            findNavController().navigate(action)
        }

        binding.fabAdd.setOnClickListener {
            val action =  MyAnnouncementsFragmentDirections.actionMyAnnouncementsFragmentToAddBoatFragment()
            findNavController().navigate(action)
        }

        binding.addBoatBtn.setOnClickListener {
                val action = MyAnnouncementsFragmentDirections.actionMyAnnouncementsFragmentToAddBoatFragment()
                findNavController().navigate(action)
        }
    }

    //Util.fDatabase.collection("BoatAnnouncement").document(Util.getUID()!!).get()

/*
    private fun getMyAnnouncementsFirestore() {
        Util.fDatabase.collection(Util.getUID()!!).get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot) {
                    val announcement = document.toObject(Announcement::class.java)
                    myAnnouncementList.add(announcement)
//                    Log.d("Firestore", announcement.toString())
                }
                myAnnouncementAdapter.notifyDataSetChanged()
        }



        Util.fDatabase.collection("BoatAnnouncement").document(Util.getUID()!!).collection("Announcement").get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot) {
                    val announcement = document.toObject(Announcement::class.java)
                    myAnnouncementList.add(announcement)
//                    Log.d("Firestore", announcement.toString())
                }
                myAnnouncementAdapter.notifyDataSetChanged()
            }

    }



    private fun getMyAnnouncements() {
        Util.mDatabase.child("announcements").child(Util.getUID()!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    myAnnouncementList.clear()

                    for (snapshot in dataSnapshot.children) {
                        val announcement = snapshot.getValue(
                            Announcement::class.java
                        )
                        myAnnouncementList.add(announcement!!)
                    }

//                    Log.d("announcementList", myAnnouncementList.toString())
                    myAnnouncementAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Firebase", databaseError.toString())
                }
            })
    }

 */
}