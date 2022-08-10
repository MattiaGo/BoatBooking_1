package com.example.boatbooking_1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.ActivityMainBinding.bind
import com.example.boatbooking_1.databinding.ActivityMainBinding.inflate
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.databinding.FragmentSearchFilterBinding
import com.example.boatbooking_1.viewmodel.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList


class SearchFilterFragment : Fragment() {
    private lateinit var binding: FragmentSearchFilterBinding
    private val searchViewModel: SearchViewModel by activityViewModels()

    private var startDate: Date? = null
    private var endDate: Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //startDate = it.getSerializable("startDate") as Date?
            //endDate = it.getSerializable("startDate") as Date?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchFilterBinding.inflate(inflater, container, false)

        if(searchViewModel.searchData.lvYear != null && searchViewModel.searchData.hvYear != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvYear!!.toFloat())
            myList.add(searchViewModel.searchData.hvYear!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvYear!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvYear!!.toString())
            binding.sliderYear.values = myList
        }

        if(searchViewModel.searchData.lvLength != null && searchViewModel.searchData.hvLength != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvLength!!.toFloat())
            myList.add(searchViewModel.searchData.hvLength!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvLength!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvLength!!.toString())
            binding.sliderLength.values = myList
        }

        if(searchViewModel.searchData.lvPassengers != null && searchViewModel.searchData.hvPassengers != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvPassengers!!.toFloat())
            myList.add(searchViewModel.searchData.hvPassengers!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvPassengers!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvPassengers!!.toString())
            binding.sliderMaxPassengers.values = myList
        }

        if(searchViewModel.searchData.lvBeds != null && searchViewModel.searchData.hvBeds != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvBeds!!.toFloat())
            myList.add(searchViewModel.searchData.hvBeds!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvBeds!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvBeds!!.toString())
            binding.sliderBeds.values = myList
        }

        if(searchViewModel.searchData.lvCabins != null && searchViewModel.searchData.hvCabins != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvCabins!!.toFloat())
            myList.add(searchViewModel.searchData.hvCabins!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvCabins!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvCabins!!.toString())
            binding.sliderCabins.values = myList
        }

        if(searchViewModel.searchData.lvBath != null && searchViewModel.searchData.hvBath != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvBath!!.toFloat())
            myList.add(searchViewModel.searchData.hvBath!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvBath!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvBath!!.toString())
            binding.sliderBathroom.values = myList
        }

        if(searchViewModel.searchData.licenceNeeded != null){
            binding.checkBoxLicense.isChecked = searchViewModel.searchData.licenceNeeded!!
        }

        if(searchViewModel.searchData.captainNeeded != null){
            binding.checkBoxCaptainNeeded.isChecked = searchViewModel.searchData.captainNeeded!!
        }

        if(searchViewModel.searchData.lvPrice != null && searchViewModel.searchData.hvPrice != null){
            val myList = mutableListOf<Float>()
            myList.add(searchViewModel.searchData.lvPrice!!.toFloat())
            myList.add(searchViewModel.searchData.hvPrice!!.toFloat())

            binding.lvYear.setText(searchViewModel.searchData.lvPrice!!.toString())
            binding.hvYear.setText(searchViewModel.searchData.hvPrice!!.toString())
            binding.sliderPrice.values = myList
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.searchData.lvYear = binding.sliderYear.values[0].toInt()
        searchViewModel.searchData.hvYear = binding.sliderYear.values[1].toInt()
        searchViewModel.searchData.lvLength = binding.sliderLength.values[0].toInt()
        searchViewModel.searchData.hvLength = binding.sliderLength.values[1].toInt()
        searchViewModel.searchData.lvPassengers = binding.sliderMaxPassengers.values[0].toInt()
        searchViewModel.searchData.hvPassengers = binding.sliderMaxPassengers.values[1].toInt()
        searchViewModel.searchData.lvBeds = binding.sliderBeds.values[0].toInt()
        searchViewModel.searchData.hvBeds = binding.sliderBeds.values[1].toInt()
        searchViewModel.searchData.lvCabins = binding.sliderCabins.values[0].toInt()
        searchViewModel.searchData.hvCabins = binding.sliderCabins.values[1].toInt()
        searchViewModel.searchData.lvBath = binding.sliderBathroom.values[0].toInt()
        searchViewModel.searchData.hvBath = binding.sliderBathroom.values[1].toInt()
        searchViewModel.searchData.licenceNeeded = binding.checkBoxLicense.isChecked
        searchViewModel.searchData.captainNeeded = binding.checkBoxCaptainNeeded.isChecked
        searchViewModel.searchData.lvPrice = binding.sliderPrice.values[0].toInt()
        searchViewModel.searchData.hvPrice = binding.sliderPrice.values[1].toInt()


        binding.sliderYear.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvYear.setText(rangeSlider.values[0].toInt().toString())
            binding.hvYear.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvYear = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvYear = rangeSlider.values[1].toInt()
        }

        binding.sliderLength.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvLength.setText(rangeSlider.values[0].toInt().toString())
            binding.hvLength.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvLength = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvLength = rangeSlider.values[1].toInt()
        }

        binding.sliderMaxPassengers.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvPassengers.setText(rangeSlider.values[0].toInt().toString())
            binding.hvPassengers.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvPassengers = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvPassengers = rangeSlider.values[1].toInt()
        }

        binding.sliderBeds.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvBeds.setText(rangeSlider.values[0].toInt().toString())
            binding.hvBeds.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvBeds = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvBeds = rangeSlider.values[1].toInt()
        }

        binding.sliderCabins.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvCabins.setText(rangeSlider.values[0].toInt().toString())
            binding.hvCabins.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvCabins = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvCabins = rangeSlider.values[1].toInt()
        }

        binding.sliderBathroom.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvBath.setText(rangeSlider.values[0].toInt().toString())
            binding.hvBath.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvBath = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvBath = rangeSlider.values[1].toInt()
        }

        binding.checkBoxLicense.setOnCheckedChangeListener { buttonView, isChecked ->
            searchViewModel.searchData.licenceNeeded = isChecked
        }

        binding.checkBoxCaptainNeeded.setOnCheckedChangeListener { buttonView, isChecked ->
            searchViewModel.searchData.captainNeeded = isChecked
        }

        binding.sliderPrice.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvPrice.setText(rangeSlider.values[0].toInt().toString())
            binding.hvPrice.setText(rangeSlider.values[1].toInt().toString())

            searchViewModel.searchData.lvPrice = rangeSlider.values[0].toInt()
            searchViewModel.searchData.hvPrice = rangeSlider.values[1].toInt()
        }
        
        binding.backBtn.setOnClickListener{
            val action = SearchFilterFragmentDirections.actionSearchFilterFragmentToMainSearch()
            findNavController().navigate(action)
        }

        binding.btnNext.setOnClickListener{
            val action = SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchResultsFragment()
            findNavController().navigate(action)
        }

        binding.resetFilterParam.setOnClickListener {
            searchViewModel.resetFilterParam()
            findNavController().navigate(R.id.action_searchFilterFragment_to_main_search)
            findNavController().navigate(R.id.action_main_search_to_searchFilterFragment)
        }

    }

}