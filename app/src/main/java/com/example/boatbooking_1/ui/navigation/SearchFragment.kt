package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.AnnouncementDetailsFragmentDirections
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentMessagesBinding
import com.example.boatbooking_1.databinding.FragmentSearchBinding
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.BookingFragmentDirections
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.BookingViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var binding: FragmentSearchBinding
    private lateinit var locationList: Array<String>
    private lateinit var locationAdapter: ArrayAdapter<String>

    private val bookingViewModel: BookingViewModel by activityViewModels()
    private lateinit var observer: androidx.lifecycle.Observer<Booking>
    private lateinit var availabilityObserver: androidx.lifecycle.Observer<Boolean>

    private var startDate: Date? = null
    private var endDate: Date? = null
    private var availability: Boolean? = null

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
        //binding.lvLocation.adapter = locationAdapter

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


        val sdf = Util.sdfBooking()

        //startDate = bookingViewModel.getBooking().value!!.startDate
        //endDate = bookingViewModel.getBooking().value!!.endDate

        /*if (startDate != null && endDate != null) {
            binding.dateContainer.isVisible = true
            binding.tvStartDate.text = sdf.format(startDate!!).toString()
            binding.tvEndDate.text = sdf.format(endDate!!).toString()

         */

//            bookingViewModel.isPeriodAvailable(startDate!!, endDate!!, AID!!, context!!)

           binding.btnContinue.setOnClickListener {
                if (!validateDate()) {
                    Toast.makeText(
                        context,
                        "Inserisci un periodo in cui vuoi effettuare la prenotazione!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                        val action =
                            SearchFragmentDirections.actionMainSearchToSearchFilterFragment()
                        findNavController().navigate(action)
                }
            }
            Log.d("MyDebug", "${startDate.toString()} ${endDate.toString()}")
        //}

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.btnContinue.setOnClickListener{
            val action =
                SearchFragmentDirections.actionMainSearchToSearchFilterFragment()
            findNavController().navigate(action)
        }

         */

        binding.btnSelectDate.setOnClickListener {
//            bookingViewModel.resetAvailability()
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
    }

    private fun validateDate(): Boolean {
//        Log.d(
//            "MyDebug", (binding.tvStartDate.text.trim().isEmpty() ||
//                    binding.tvEndDate.text.trim().isEmpty()).toString()
//        )
        return binding.tvStartDate.text.trim().isNotEmpty() ||
                binding.tvEndDate.text.trim().isNotEmpty()
    }
}