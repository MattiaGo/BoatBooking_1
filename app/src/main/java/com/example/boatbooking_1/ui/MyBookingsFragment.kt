package com.example.boatbooking_1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.FragmentMyBookingsBinding
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.viewmodel.BookingViewModel

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

    private lateinit var myBookingPastList: ArrayList<Booking>
    private lateinit var myBookingPastAdapter: MyBookingAdapter
    private lateinit var myBookingList: ArrayList<Booking>
    private lateinit var myBookingAdapter: MyBookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myBookingList = ArrayList()
        myBookingAdapter = MyBookingAdapter(myBookingList, requireContext())

        myBookingPastList = ArrayList()
        myBookingPastAdapter = MyBookingAdapter(myBookingPastList, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBookingsBinding.inflate(inflater, container, false)

//        bookingViewModel.getUserBookingList(myBookingPastList, myBookingPastAdapter)
        bookingViewModel.getUserBookingList(myBookingList, myBookingAdapter)

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