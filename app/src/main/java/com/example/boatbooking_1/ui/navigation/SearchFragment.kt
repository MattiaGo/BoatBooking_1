package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.SearchViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var binding: FragmentSearchBinding
    private lateinit var locationAdapter: ArrayAdapter<String>

    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var locationObserver: Observer<ArrayList<String>>

    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onStart() {
        super.onStart()
        searchViewModel.getLocationsFromDatabase()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            searchViewModel.locationList.value!!
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.dateContainer.isVisible = false

        searchView = binding.searchView

        locationObserver = Observer<ArrayList<String>> { locations ->

            Log.d("Search", "Observer!")

            locationAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                locations
            )

            binding.lvLocation.adapter = locationAdapter

            locationAdapter.notifyDataSetChanged()
        }

        searchViewModel.locationList.observe(viewLifecycleOwner, locationObserver)

        val sdf = Util.sdfBooking()

        if (searchViewModel.searchData.location != null) {
            binding.searchView.setQuery(searchViewModel.searchData.location!!, true)
        }

        if (searchViewModel.searchData.startDate != null && searchViewModel.searchData.endDate != null) {
            binding.tvStartDate.text = sdf.format(searchViewModel.searchData.startDate!!).toString()
            binding.tvEndDate.text = sdf.format(searchViewModel.searchData.endDate!!).toString()
            binding.dateContainer.isVisible = true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                binding.datePickerLayout.isVisible = false

                if (searchViewModel.locationList.value!!.contains(query)) {
                    locationAdapter.filter.filter(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.datePickerLayout.isVisible = false
                binding.lvLocation.isVisible = true
                locationAdapter.filter.filter(newText)

                if (newText!!.isEmpty()) {
                    binding.lvLocation.isVisible = false
                    binding.datePickerLayout.isVisible = true
                }
                return false
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectDate.setOnClickListener {
            val sdf = Util.sdfBooking()

            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()

            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Seleziona una data o un periodo")
                    .setPositiveButtonText("Salva")
                    .setCalendarConstraints(constraintsBuilder)
                    .build()

            dateRangePicker.show(childFragmentManager, "BookingFragment")

//            Log.d("Booking", announcementViewModel.getAnnouncement().value.toString())

            dateRangePicker.addOnPositiveButtonClickListener {
                Log.d("DatePicker", dateRangePicker.selection.toString())

                binding.tvStartDate.text = sdf.format(Date(dateRangePicker.selection!!.first))
                binding.tvEndDate.text = sdf.format(Date(dateRangePicker.selection!!.second))

                startDate = Date(dateRangePicker.selection!!.first)
                endDate = Date(dateRangePicker.selection!!.second)

                searchViewModel.searchData.startDate = startDate
                searchViewModel.searchData.endDate = endDate

                Log.d("Booking", startDate.toString() + "\n${endDate.toString()}")

                binding.dateContainer.isVisible = true
                //binding.tvNotAvailable.isVisible = false
            }

            dateRangePicker.addOnCancelListener {
                Log.d("DatePicker", "Cancel: $it")
            }

            dateRangePicker.addOnDismissListener {
                Log.d("DatePicker", "Dismiss: $it")
            }

            dateRangePicker.addOnNegativeButtonClickListener {
                Log.d("DatePicker", "Negative: $it")
            }
        }

        binding.lvLocation.setOnItemClickListener { locationAdapter, it, i, l ->
            binding.searchView.setQuery(binding.lvLocation.getItemAtPosition(i).toString(), true)
            searchViewModel.searchData.location = binding.searchView.query.toString().toUpperCase()
            binding.lvLocation.isVisible = false
            binding.datePickerLayout.isVisible = true
        }

        binding.btnContinue.setOnClickListener {
            if (!validateLocation()) {
                Toast.makeText(
                    context,
                    "Inserisci un luogo dove cercare una barca",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!validateDate()) {
                    Toast.makeText(
                        context,
                        "Inserisci un periodo in cui vuoi effettuare la prenotazione!",
                        Toast.LENGTH_SHORT
                    ).show()
            } else {
                val action = SearchFragmentDirections.actionMainSearchToSearchFilterFragment()
                findNavController().navigate(action)
            }
        }
        Log.d("MyDebug", "${startDate.toString()} ${endDate.toString()}")

        binding.resetAllParam.setOnClickListener {
            searchViewModel.resetData()
            binding.searchView.setQuery("", true)
            binding.tvStartDate.text = ""
            binding.tvEndDate.text = ""
            binding.dateContainer.isVisible = false
        }
    }

    private fun validateDate(): Boolean {
//        Log.d(
//            "MyDebug", (binding.tvStartDate.text.trim().isEmpty() ||
//                    binding.tvEndDate.text.trim().isEmpty()).toString()
//        )
        return searchViewModel.searchData.startDate != null ||
                searchViewModel.searchData.endDate != null
    }

    private fun validateLocation(): Boolean {
//        Log.d(
//            "MyDebug", (binding.tvStartDate.text.trim().isEmpty() ||
//                    binding.tvEndDate.text.trim().isEmpty()).toString()
//        )
        return  searchViewModel.searchData.location != null
    }
}