package com.example.boatbooking_1.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.FragmentMyAnnouncementsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.utils.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAnnouncementsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAnnouncementsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMyAnnouncementsBinding
    private lateinit var observer: Observer<ArrayList<Announcement>>

    private lateinit var myAnnouncementAdapter: MyAnnouncementAdapter
    private lateinit var myAnnouncementList: ArrayList<Announcement>

    private lateinit var rvMyAnnouncements: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var btnBack: ImageButton

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
        fabAdd = binding.fabAdd
        btnBack = binding.btnBack
        rvMyAnnouncements = binding.rvMyAnnouncements

        rvMyAnnouncements.layoutManager = LinearLayoutManager(this.context)
        rvMyAnnouncements.adapter = myAnnouncementAdapter

//        getMyAnnouncements() // Realtime Database
        getMyAnnouncementsFirestore()

        Log.d("myAnnouncements", myAnnouncementList.toString())

        fabAdd.setOnClickListener {
            val action =
                MyAnnouncementsFragmentDirections.actionMyAnnouncementsFragmentToAddBoatFragment()
            findNavController().navigate(action)
        }

        btnBack.setOnClickListener {
            val action =
                MyAnnouncementsFragmentDirections.actionMyAnnouncementsFragmentToUserProfile()
            findNavController().navigate(action)
        }

        return binding.root
    }

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAnnouncementsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAnnouncementsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}