package com.example.boatbooking_1.ui.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.adapter.PublicAnnouncementAdapter
import com.example.boatbooking_1.databinding.FragmentHomeBinding
import com.example.boatbooking_1.model.Announcement
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

        rvLastAdded = binding.rvLastAdded
        rvLastAdded.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvLastAdded.adapter = lastAddedAdapter

        rvRecentSeen = binding.rvRecentSeen
        rvRecentSeen.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        rvRecentSeen.adapter = lastSeenAdapter

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

}