package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentHomeBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.viewmodel.HomeAnnouncementViewModel
import com.example.boatbooking_1.viewmodel.UserProfileViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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

    private var remoteImagesURIMostRequestedList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lastAddedList = ArrayList()
        lastSeenList = ArrayList()
        mostRequestedList = ArrayList()

//        lastAddedAdapter = PublicAnnouncementAdapter(lastAddedList, requireContext(), ArrayList())
//        lastSeenAdapter = PublicAnnouncementAdapter(lastSeenList, requireContext(), ArrayList())
        mostRequestedAdapter = PublicAnnouncementAdapter(mostRequestedList, requireContext(), remoteImagesURIMostRequestedList)

        userViewModel.getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lastAddedList.clear()
        lastSeenList.clear()
        mostRequestedList.clear()

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //BEST CHARTER
//        rvLastAdded = binding.rvLastAdded
//        rvLastAdded.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        rvLastAdded.adapter = lastAddedAdapter

        //LAST SEEN
//        rvRecentSeen = binding.rvRecentSeen
//        rvRecentSeen.layoutManager =
//            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        rvRecentSeen.adapter = lastSeenAdapter

        //MOST REQ LOCATION
        rvMostRequested = binding.rvMostRequested
        rvMostRequested.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvMostRequested.adapter = mostRequestedAdapter

//        homeAnnouncementViewModel.getLastAddedAnnouncement(
//            lastAddedList,
//            lastAddedAdapter
//        )

        homeAnnouncementViewModel.getMostRequestedAnnouncement(
            mostRequestedList,
            mostRequestedAdapter,
            remoteImagesURIMostRequestedList
        )

        return binding.root
    }

}