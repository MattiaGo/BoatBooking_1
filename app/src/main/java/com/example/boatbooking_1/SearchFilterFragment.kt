package com.example.boatbooking_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.ActivityMainBinding.inflate
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.databinding.FragmentSearchFilterBinding


class SearchFilterFragment : Fragment() {
    private lateinit var binding: FragmentSearchFilterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
            val action = SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchResultsFragment()
            findNavController().navigate(action)
        }
    }

}