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
import com.example.boatbooking_1.adapter.MyBookingAdapter
import com.example.boatbooking_1.databinding.FragmentMyBookingsBinding
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.viewmodel.BookingViewModel
import com.example.boatbooking_1.viewmodel.MyBookingViewModel
import com.example.boatbooking_1.viewmodel.UserProfileViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ui.MyBookingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyBookingsFragment : Fragment() {

    private lateinit var binding: FragmentMyBookingsBinding
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private val myBookingsViewModel: MyBookingViewModel by activityViewModels()
    private val userViewModel: UserProfileViewModel by activityViewModels()

    private lateinit var myPastBookingList: ArrayList<Booking>
    private lateinit var myPastBookingAdapter: MyBookingAdapter
    private lateinit var myBookingList: ArrayList<Booking>
    private lateinit var myBookingAdapter: MyBookingAdapter
    private lateinit var myBookingsObserver: Observer<ArrayList<Booking>>
    private lateinit var myPastBookingsObserver: Observer<ArrayList<Booking>>

    private lateinit var rvMyBookings: RecyclerView
    private lateinit var rvMyPastBookings: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myBookingList = ArrayList()
        myPastBookingList = ArrayList()

        myPastBookingAdapter = MyBookingAdapter(
            myPastBookingList,
            requireContext(),
            myBookingsViewModel,
            userViewModel
        )

        myBookingAdapter = MyBookingAdapter(
            myBookingList,
            requireContext(),
            myBookingsViewModel,
            userViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        rvMyBookings = binding.rvMyCurrentBookings
        rvMyPastBookings = binding.rvMyPastBookings

//        bookingViewModel.getUserBookingList(myBookingPastList, myBookingPastAdapter)

        if (userViewModel.getUser().value?.shipOwner == true) {
            myBookingsViewModel.setOwnerBookings(myBookingAdapter, myPastBookingAdapter)
            Log.d("MyBookings", userViewModel.getUser().value?.shipOwner.toString())
        }

        myBookingsObserver = Observer<ArrayList<Booking>> { booking ->

            myBookingList = booking

            myBookingAdapter = MyBookingAdapter(
                myBookingList,
                requireContext(),
                myBookingsViewModel,
                userViewModel
            )
            rvMyBookings.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvMyBookings.adapter = myBookingAdapter
            myBookingAdapter.notifyDataSetChanged()
        }

        myPastBookingsObserver = Observer<ArrayList<Booking>> { booking ->
            myPastBookingList = booking

            myPastBookingAdapter = MyBookingAdapter(
                myPastBookingList,
                requireContext(),
                myBookingsViewModel,
                userViewModel
            )
            rvMyPastBookings.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvMyPastBookings.adapter = myPastBookingAdapter
            myPastBookingAdapter.notifyDataSetChanged()
        }

        myBookingsViewModel.bookingLiveData.observe(viewLifecycleOwner, myBookingsObserver)
        myBookingsViewModel.pastBookingLiveData.observe(viewLifecycleOwner, myPastBookingsObserver)

        binding.backBtn.setOnClickListener {
            val action = MyBookingsFragmentDirections.actionMyBookingsFragmentToUserProfile()
            findNavController().navigate(action)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyBookingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}