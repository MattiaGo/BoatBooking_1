package com.example.boatbooking_1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.databinding.FragmentSearchResultsBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.FavoritesAnnouncementAdapter
import com.example.boatbooking_1.ui.SearchResultAnnouncementAdapter
import com.example.boatbooking_1.viewmodel.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList



class SearchResultsFragment : Fragment() {
    private lateinit var binding: FragmentSearchResultsBinding

    private lateinit var resultAdapter: SearchResultAnnouncementAdapter
    private lateinit var resultList: ArrayList<Announcement>
    private lateinit var rvResults: RecyclerView

    private var remoteImagesURIList: MutableList<String> = mutableListOf()
    private val searchViewModel: SearchViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        resultList = ArrayList()
        resultAdapter = SearchResultAnnouncementAdapter(resultList,requireContext(),remoteImagesURIList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)

        resultList.clear()
        remoteImagesURIList.clear()

        rvResults = binding.rvSearchResults
        rvResults.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvResults.adapter = resultAdapter

        searchViewModel.searchAnnouncement(
            resultList,
            searchViewModel.searchData,
            resultAdapter,
            remoteImagesURIList)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener{
            val action = SearchResultsFragmentDirections.actionSearchResultsFragmentToSearchFilterFragment()
            findNavController().navigate(action)
        }
    }


}