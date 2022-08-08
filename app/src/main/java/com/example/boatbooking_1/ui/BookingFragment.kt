package com.example.boatbooking_1.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentBookingBinding
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel
import com.example.boatbooking_1.viewmodel.BookingViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBookingBinding
    private val announcementViewModel: AnnouncementViewModel by activityViewModels()
    private val bookingViewModel: BookingViewModel by activityViewModels()
    private lateinit var observer: Observer<Booking>
    private lateinit var availabilityObserver: Observer<Boolean>

    private var startDate: Date? = null
    private var endDate: Date? = null
    private var availability: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater, container, false)

        val AID = announcementViewModel.getAnnouncement().value!!.id
        Log.d("MyDebug", "AID: $AID")
        availability = bookingViewModel.getAvailability().value

        observer = Observer<Booking> { booking ->
//            Log.d(
//                "Booking",
//                "startDate: ${bookingViewModel.getBooking().value?.startDate.toString()}" +
//                        "\nendDate: ${bookingViewModel.getBooking().value?.endDate.toString()}"
//            )
        }

        binding.backBtn.setOnClickListener {
            val action =
                BookingFragmentDirections.actionBookingFragmentToAnnouncementDetailsFragment()
            val bundle = Bundle()
            bundle.putString("id", announcementViewModel.getAnnouncement().value!!.id)

            findNavController().navigate(R.id.announcementDetailsFragment, bundle)
        }

        val sdf = Util.sdfBooking()

        startDate = bookingViewModel.getBooking().value!!.startDate
        endDate = bookingViewModel.getBooking().value!!.endDate

        if (startDate != null && endDate != null) {
            binding.dateContainer.isVisible = true
            binding.tvStartDate.text = sdf.format(startDate!!).toString()
            binding.tvEndDate.text = sdf.format(endDate!!).toString()

//            bookingViewModel.isPeriodAvailable(startDate!!, endDate!!, AID!!, context!!)

            binding.btnContinue.setOnClickListener {
//            Log.d("MyDebug", validateDate().toString())
                if (!validateDate()) {
                    Toast.makeText(
                        context,
                        "Inserisci un periodo in cui vuoi effettuare la prenotazione!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (availability!!) {
                        bookingViewModel.setBookingDate(startDate!!, endDate!!)
                        val action =
                            BookingFragmentDirections.actionBookingFragmentToBookingServicesFragment()
                        findNavController().navigate(action)
                        Log.d("MyDebug", "PERIOD OK")
                    } else {
                        Log.d("MyDebug", "PERIOD NOT OK")
                        binding.tvNotAvailable.isVisible = true
                    }
                }
            }

            Log.d("MyDebug", "${startDate.toString()} ${endDate.toString()}")
        }

        bookingViewModel.getBooking().observe(viewLifecycleOwner, observer)

        binding.btnSelectDate.setOnClickListener {
//            bookingViewModel.resetAvailability()

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

                bookingViewModel.isPeriodAvailable(
                    startDate!!,
                    endDate!!,
                    AID!!,
                    requireContext()
                )

                binding.dateContainer.isVisible = true
                binding.tvNotAvailable.isVisible = false
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

        availabilityObserver = Observer<Boolean> { available ->
            availability = available
            Log.d("MyDebug", "Observer: $availability")

                binding.btnContinue.setOnClickListener {
//            Log.d("MyDebug", validateDate().toString())
                    if (!validateDate()) {
                        Toast.makeText(
                            context,
                            "Inserisci un periodo in cui vuoi effettuare la prenotazione!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (availability!!) {
                            bookingViewModel.setBookingDate(startDate!!, endDate!!)
                            val action =
                                BookingFragmentDirections.actionBookingFragmentToBookingServicesFragment()
                            findNavController().navigate(action)
                            Log.d("MyDebug", "PERIOD OK")
                        } else {
                            Log.d("MyDebug", "PERIOD NOT OK")
                            binding.tvNotAvailable.isVisible = true
                        }
                    }
                }

            }

            bookingViewModel.getAvailability().observe(viewLifecycleOwner, availabilityObserver)
        }

//        binding.btnContinue.isEnabled = false

        return binding.root
//        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    private fun validateDate(): Boolean {
//        Log.d(
//            "MyDebug", (binding.tvStartDate.text.trim().isEmpty() ||
//                    binding.tvEndDate.text.trim().isEmpty()).toString()
//        )
        return binding.tvStartDate.text.trim().isNotEmpty() ||
                binding.tvEndDate.text.trim().isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}