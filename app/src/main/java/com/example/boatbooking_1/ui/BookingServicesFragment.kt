package com.example.boatbooking_1.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.adapter.BookingServiceAdapter
import com.example.boatbooking_1.databinding.FragmentBookingServicesBinding
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.navigation.SearchFragmentDirections
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.example.boatbooking_1.viewmodel.BookingViewModel
import com.example.boatbooking_1.viewmodel.SearchViewModel
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingServicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingServicesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBookingServicesBinding
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var announcementObserver: Observer<Announcement>
    private lateinit var bookingObserver: Observer<Booking>
    private lateinit var bookingServiceAdapter: BookingServiceAdapter
    private lateinit var rvBookingServices: RecyclerView
    private lateinit var bookingServiceList: ArrayList<BoatService>

//    private var total: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookingServiceList = ArrayList()

//        bookingServiceAdapter = BookingServiceAdapter(bookingServiceList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_booking_services, container, false)
        binding = FragmentBookingServicesBinding.inflate(inflater, container, false)

        rvBookingServices = binding.rvBookingServices
//        rvBookingServices.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        rvBookingServices.adapter = bookingServiceAdapter
        val searchFlag = arguments?.getBoolean("search")

        announcementObserver = Observer<Announcement> { announcement ->

//            bookingServiceList.clear()
//            bookingServiceList.addAll(announcement.services!!)
//            bookingServiceAdapter.notifyDataSetChanged()

            binding.tvPrice.text = announcement.price.toString()

            //bookingViewModel.setBookingDate(searcViewModel.searchData.startDate!!, searcViewModel.searchData.endDate!!)
            if (searchFlag == true) {
                bookingViewModel.setAvailability(true)
                bookingViewModel.setBookingDate(
                    searchViewModel.searchData.startDate!!,
                    searchViewModel.searchData.endDate!!
                )
            }

            bookingViewModel.setBasePrice(announcement.price.toString().toInt())

            bookingServiceList = announcement.services!!
            bookingServiceAdapter = BookingServiceAdapter(
                bookingServiceList,
                bookingViewModel
            )
            rvBookingServices.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvBookingServices.adapter = bookingServiceAdapter
            bookingServiceAdapter.notifyDataSetChanged()

//            rvBookingServices.adapter = bookingServiceAdapter
//            rvBookingServices.swapAdapter(bookingServiceAdapter, false)
//            rvBookingServices.layoutManager
//            rvBookingServices.recycledViewPool.clear()

            Log.d("BookingService", bookingServiceList.toString())

        }

//        bookingServiceList.addAll(announcementViewModel.getAnnouncement().value!!.services!!)
//        bookingServiceAdapter.notifyDataSetChanged()

        bookingObserver = Observer<Booking> { booking ->
            Log.d(
                "BookingService",
                "${booking.startDate.toString()}\n ${booking.startDate.toString()}"
            )

            Log.d("BookingService", "Observer: ${booking.total}")

            binding.tvTotalPrice.text = booking.total.toString()

            booking.announcement = announcementViewModel.getAnnouncement().value

        }

        announcementViewModel.getAnnouncement().observe(viewLifecycleOwner, announcementObserver)
        bookingViewModel.getBooking().observe(viewLifecycleOwner, bookingObserver)

//        Log.d("BookingService", "StateAfter: ${viewLifecycleOwner.lifecycle.currentState}")

        binding.backBtn.setOnClickListener {
            val action =
                BookingServicesFragmentDirections.actionBookingServicesFragmentToBookingFragment()
            findNavController().navigate(action)
        }

        binding.btnNext.setOnClickListener {
            Log.d("Booking", bookingViewModel.getBooking().value.toString())
            bookingViewModel.addBookingOnDatabase(announcementViewModel.getAnnouncement().value!!.id!!)
            bookingViewModel.incrementReservationCounter((announcementViewModel.getAnnouncement().value!!))

            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "Prenotazione effettuata con successo!", Snackbar.LENGTH_LONG
            )
                .setAnchorView(com.example.boatbooking_1.R.id.bottom_nav)
                .setAction("HO CAPITO") { // Responds to click on the action
                }.show()

            if (searchFlag == true) {
                findNavController().navigate(R.id.action_bookingServicesFragment_to_main_search)
            } else {
                val action = BookingServicesFragmentDirections.actionBookingServicesFragmentToMainHome()
                findNavController().navigate(action)
            }

        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingServicesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingServicesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}