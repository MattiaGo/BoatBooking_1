package com.example.boatbooking_1.repository

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.MyBookingAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.toObject
import java.util.*
import kotlin.collections.ArrayList
import me.moallemi.tools.daterange.date.rangeTo

class BookingRepository {
    private var _bookingLiveData = MutableLiveData<Booking>()
    val bookingLiveData: LiveData<Booking>
        get() = _bookingLiveData

    private var _available = MutableLiveData<Boolean>()
    val available: LiveData<Boolean>
        get() = _available

    init {
        resetLiveData()
        resetAvailability()
    }

    fun resetAvailability() {
        _available = MutableLiveData()
    }

    private fun resetLiveData() {
        _bookingLiveData = MutableLiveData()
        val booking = Booking()
        _bookingLiveData.value = booking
    }

    fun setBookingDate(start: Date, end: Date) {
        if (_available.value!!) {
            _bookingLiveData.value?.startDate = start
            _bookingLiveData.value?.endDate = end
        }
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

    fun updateBoatServiceList(service: BoatService, toAdd: Boolean) {
        if (toAdd)
            _bookingLiveData.value!!.services!!.add(service)
        else _bookingLiveData.value!!.services!!.remove(service)
    }

    fun addBookingOnDatabase(AID: String) {
        val idShipOwner = _bookingLiveData.value!!.announcement!!.id_owner
        val BID = Util.getUID().plus("@${Timestamp.now().seconds}")

        val booking = _bookingLiveData.value
        booking!!.id = BID
        booking.idShipOwner = idShipOwner
        booking.AID = AID

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

    fun updateBookingTotal(addPrice: Int) {
        val bookingUpdated = _bookingLiveData.value
        bookingUpdated!!.total += addPrice

        _bookingLiveData.value = bookingUpdated
    }

    fun isPeriodAvailable(startDate: Date, endDate: Date, AID: String, context: Context) {
        val rangeInputDate = startDate.rangeTo(endDate)

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Verifica della disponibilità...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Util.fDatabase.collectionGroup("Booking")
            .whereEqualTo("aid", AID)
            .get()
            .addOnSuccessListener {
//                var availableUpdated = _available.value
                if (it.isEmpty) {
                    _available.value = it.isEmpty
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                } else {
                    var available = true

                    loop@for (document in it) {
                        val booking = document.toObject(Booking::class.java)
                        val rangeDate = booking.startDate!!.rangeTo(booking.endDate!!)

                        for (date in rangeDate) {
//                            Log.d("MyDebug", "for: $date}")
                            if (date in rangeInputDate) {
//                                Log.d("MyDebug", "if: $date}")
                                available = false
                                break@loop
                            }
                        }
//                    Log.d("MyDebug", booking.toString())
                    }
                    _available.value = available
                    if (progressDialog.isShowing)
                        progressDialog.dismiss()
                }
            }
            .addOnFailureListener {
                if (progressDialog.isShowing)
                    progressDialog.dismiss()

                Log.d("Booking", "Error: $it")
            }
    }

    companion object {
        private var bookingRepository: BookingRepository? = null
        val instance: BookingRepository
            get() = BookingRepository().also { bookingRepository = it }
    }
}