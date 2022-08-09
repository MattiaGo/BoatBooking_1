package com.example.boatbooking_1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.ActivityMainBinding.inflate
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.databinding.FragmentSearchFilterBinding
import java.util.*
import kotlin.collections.ArrayList


class SearchFilterFragment : Fragment() {
    private lateinit var binding: FragmentSearchFilterBinding

    private var startDate: Date? = null
    private var endDate: Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startDate = it.getSerializable("startDate") as Date?
            endDate = it.getSerializable("startDate") as Date?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchFilterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sliderYear.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvYear.setText(rangeSlider.values[0].toInt().toString())
            binding.hvYear.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.sliderLength.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvLength.setText(rangeSlider.values[0].toInt().toString())
            binding.hvLength.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.sliderMaxPassengers.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvPassengers.setText(rangeSlider.values[0].toInt().toString())
            binding.hvPassengers.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.sliderBeds.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvBeds.setText(rangeSlider.values[0].toInt().toString())
            binding.hvBeds.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.sliderCabins.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvCabins.setText(rangeSlider.values[0].toInt().toString())
            binding.hvCabins.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.sliderBathroom.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvBath.setText(rangeSlider.values[0].toInt().toString())
            binding.hvBath.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.sliderPrice.addOnChangeListener { rangeSlider, value, fromUser ->
            binding.lvPrice.setText(rangeSlider.values[0].toInt().toString())
            binding.hvPrice.setText(rangeSlider.values[1].toInt().toString())
        }

        binding.backBtn.setOnClickListener{
            val action = SearchFilterFragmentDirections.actionSearchFilterFragmentToMainSearch()
            findNavController().navigate(action)
        }

        binding.btnNext.setOnClickListener{
            val bundle = Bundle()
            //bundle.putString("location", binding.)
            bundle.putSerializable("startDate", startDate)
            bundle.putSerializable("endDate", endDate)

            bundle.putInt("lvYear", binding.lvYear.text.toString().toInt())
            bundle.putInt("hvYear", binding.hvYear.text.toString().toInt())

            bundle.putInt("lvLength", binding.lvLength.text.toString().toInt())
            bundle.putInt("hvLength", binding.hvLength.text.toString().toInt())

            bundle.putInt("lvPassengers", binding.lvPassengers.text.toString().toInt())
            bundle.putInt("hvPassengers", binding.hvPassengers.text.toString().toInt())

            bundle.putInt("lvBeds", binding.lvBeds.text.toString().toInt())
            bundle.putInt("hvBeds", binding.hvBeds.text.toString().toInt())

            bundle.putInt("lvCabins", binding.lvCabins.text.toString().toInt())
            bundle.putInt("hvCabins", binding.hvCabins.text.toString().toInt())

            bundle.putInt("lvBath", binding.lvBath.text.toString().toInt())
            bundle.putInt("hvBath", binding.hvBath.text.toString().toInt())

            bundle.putBoolean("licenceNeeded", binding.checkBoxLicense.isChecked)
            bundle.putBoolean("captainNeeded", binding.checkBoxCaptainNeeded.isChecked)

            bundle.putInt("lvPrice", binding.lvPrice.text.toString().toInt())
            bundle.putInt("hvPrice", binding.hvPrice.text.toString().toInt())

            findNavController().navigate(R.id.action_searchFilterFragment_to_searchResultsFragment, bundle)
        }
    }

}