package com.example.boatbooking_1.viewmodel

import android.icu.util.Calendar
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.MyBookingAdapter
import com.example.boatbooking_1.utils.Util
import java.util.*
import kotlin.collections.ArrayList

class MyBookingViewModel : ViewModel() {
    private var _pastBookingLiveData = MutableLiveData<ArrayList<Booking>>()
    val pastBookingLiveData: LiveData<ArrayList<Booking>>
        get() {
            if (_pastBookingLiveData.value == null) {
                val bookingList = arrayListOf<Booking>()
                _pastBookingLiveData.value = bookingList

//                val today = LocalDateTime.now()
//                Log.d("Booking", "Today: ${today}")

                Util.fDatabase.collection("BoatBookings")
                    .document(Util.getUID()!!)
                    .collection("Booking")
                    .whereLessThanOrEqualTo("endDate", Date(System.currentTimeMillis()))
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            val booking = document.toObject(Booking::class.java)
                            bookingList.add(booking)
                        }
                        _pastBookingLiveData.value = bookingList
                    }

                return _pastBookingLiveData

            } else return _pastBookingLiveData
        }

    private var _bookingLiveData = MutableLiveData<ArrayList<Booking>>()
    val bookingLiveData: LiveData<ArrayList<Booking>>
        get() {
            if (_bookingLiveData.value == null) {
                val bookingList = arrayListOf<Booking>()
                _bookingLiveData.value = bookingList

//                val today = LocalDateTime.now()
//                Log.d("Booking", "Today: ${today}")

                Util.fDatabase.collection("BoatBookings")
                    .document(Util.getUID()!!)
                    .collection("Booking")
                    .whereGreaterThanOrEqualTo("endDate", Date(System.currentTimeMillis()))
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            val booking = document.toObject(Booking::class.java)
                            bookingList.add(booking)
                        }
                        _bookingLiveData.value = bookingList
                    }

                return _bookingLiveData

            } else return _bookingLiveData
        }

    init {

    }

    fun refresh() {
        _bookingLiveData.value = null
    }

    fun removeBookingFromDatabase(booking: Booking) {
        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .collection("Booking")
            .document(booking.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d("MyBooking", "OnSuccess: Booking removed!")
            }

        refreshLiveData(booking)
    }

    private fun refreshLiveData(booking: Booking) {
        val bookingList = arrayListOf<Booking>()
        bookingList.addAll(_bookingLiveData.value!!)
        bookingList.remove(booking)

        _bookingLiveData.value = bookingList
    }

    fun setOwnerBookings(adapter: MyBookingAdapter) {
        val bookingList = arrayListOf<Booking>()

        Util.fDatabase.collectionGroup("Booking")
            .whereEqualTo("idShipOwner", Util.getUID())
            .get()
            .addOnSuccessListener {
                Log.d("MyBookings", it.isEmpty.toString())
                for (document in it) {
                    val booking = document.toObject(Booking::class.java)
                    bookingList.add(booking)
                    Log.d("MyBookings", booking.toString())
                }
                _bookingLiveData.value = bookingList
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("MyBookings", "Error: $it")
            }

    }

    fun removeAllBookings() {
        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .collection("Booking")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    document.reference.delete()
                }
            }
    }
}