package com.example.boatbooking_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.databinding.FragmentSearchResultsBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.FavoritesAnnouncementAdapter
import java.util.*
import kotlin.collections.ArrayList

private lateinit var binding: FragmentSearchResultsBinding

private lateinit var resultAdapter: FavoritesAnnouncementAdapter
private lateinit var resultList: ArrayList<Announcement>
private lateinit var rvResults: RecyclerView

private var remoteImagesURIList: MutableList<String> = mutableListOf()

private var startDate: Date? = null
private var endDate: Date? = null
private var lvYear: Int? = null
private var hvYear: Int? = null
private var lvLength: Int? = null
private var hvLength: Int? = null
private var lvPassengers: Int? = null
private var hvPassengers: Int? = null
private var lvBeds: Int? = null
private var hvBeds: Int? = null
private var lvCabins: Int? = null
private var hvCabins: Int? = null
private var lvBath: Int? = null
private var hvBath: Int? = null
private var licenceNeeded: Boolean? = false
private var captainNeeded: Boolean? = false
private var lvPrice: Int? = null
private var hvPrice: Int? = null

class SearchResultsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startDate = it.getSerializable("startDate") as Date?
            endDate = it.getSerializable("startDate") as Date?

            lvYear = it.getInt("lvYear")
            hvYear = it.getInt("hvYear")

            lvLength = it.getInt("lvLength")
            hvLength = it.getInt("hvLength")

            lvPassengers = it.getInt("lvPassengers")
            hvPassengers = it.getInt("hvPassengers")

            lvBeds = it.getInt("lvBeds")
            hvBeds = it.getInt("hvBeds")

            lvCabins = it.getInt("lvCabins")
            hvCabins = it.getInt("hvCabins")

            hvBath = it.getInt("hvBath")
            hvBath = it.getInt("hvBath")

            licenceNeeded = it.getBoolean("licenceNeeded")

            captainNeeded = it.getBoolean("captainNeeded")

            lvPrice = it.getInt("lvPrice")
            hvPrice = it.getInt("hvPrice")
        }

        resultList = ArrayList()
        resultAdapter = FavoritesAnnouncementAdapter(resultList,requireContext(),remoteImagesURIList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)

        resultList.clear()


        rvResults = binding.rvSearchResults
        rvResults.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvResults.adapter = resultAdapter


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