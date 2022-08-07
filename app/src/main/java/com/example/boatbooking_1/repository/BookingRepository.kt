package com.example.boatbooking_1.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.MyBookingAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class BookingRepository {
    private var _bookingLiveData = MutableLiveData<Booking>()
    val bookingLiveData: LiveData<Booking>
        get() = _bookingLiveData

    init {
        resetLiveData()
    }

    private fun resetLiveData() {
        _bookingLiveData = MutableLiveData()
        val booking = Booking()
        _bookingLiveData.value = booking
    }

    fun setBookingDate(start: Date, end: Date) {
        _bookingLiveData.value?.startDate = start
        _bookingLiveData.value?.endDate = end
    }

    fun getUserBookingList(
        myBookingList: ArrayList<Booking>,
        myBookingAdapter: MyBookingAdapter
    ) {
        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .collection("Booking")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val booking = document.toObject(Booking::class.java)
                    myBookingList.add(booking)
                }

                myBookingAdapter.notifyDataSetChanged()
            }
    }

    fun updateBookingTotal(addPrice: Int) {
        val bookingUpdated = _bookingLiveData.value
        bookingUpdated!!.total += addPrice

        _bookingLiveData.value = bookingUpdated
    }

    fun updateBoatServiceList(service: BoatService, toAdd: Boolean) {
        if (toAdd)
            _bookingLiveData.value!!.services!!.add(service)
        else _bookingLiveData.value!!.services!!.remove(service)
    }

    fun addBookingOnDatabase() {
        val idShipOwner = _bookingLiveData.value!!.announcement!!.id_owner
        val BID = Util.getUID().plus("@${Timestamp.now().seconds}")

        val booking = _bookingLiveData.value
        booking!!.id = BID
        booking.idShipOwner = idShipOwner

        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .collection("Booking")
            .document(BID)
            .set(booking)
            .addOnCompleteListener {
                Log.d("Booking", "OnComplete: Booking added!")
                resetLiveData()
            }
            .addOnFailureListener {
                Log.d("Booking", "Error: $it")
                resetLiveData()
            }
    }

    companion object {
        private var bookingRepository: BookingRepository? = null
        val instance: BookingRepository
            get() = BookingRepository().also { bookingRepository = it }
    }
}