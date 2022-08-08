package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentMessagesBinding
import com.example.boatbooking_1.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var binding: FragmentSearchBinding
    private lateinit var locationList: Array<String>
    private lateinit var locationAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationList = arrayOf(
            "Brescia",
            "Bergamo",
            "Milano",
            "Verona",
            "Torino"
        )

        locationAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1,
            locationList
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        searchView = binding.searchView
        binding.lvLocation.adapter = locationAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                if (locationList.contains(query)) {
                    locationAdapter.filter.filter(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                locationAdapter.filter.filter(newText)

                return false
            }

        })
        return binding.root
    }
}