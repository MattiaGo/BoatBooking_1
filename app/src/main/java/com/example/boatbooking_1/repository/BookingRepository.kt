package com.example.boatbooking_1.repository

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.adapter.MyBookingAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
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

    fun setBasePrice(price: Int) {
        val bookingUpdated = _bookingLiveData.value

        val startDate = bookingUpdated?.startDate
        val endDate = bookingUpdated?.endDate

        val diff: Long = endDate!!.time - startDate!!.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = (hours / 24).toInt() + 1

        bookingUpdated.total = (price * days)
        _bookingLiveData.value = bookingUpdated
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

    fun incrementReservationCounter(announcement: Announcement){
        //ADD +1 RESERVATION COUNTER
        Util.fDatabase.collectionGroup("Announcement")
            .whereEqualTo("id", announcement.id!!)
            .get()
            .addOnSuccessListener {
                val id_owner = it.documents[0].get("id_owner").toString()

                Util.fDatabase.collection("BoatAnnouncement")
                    .document(id_owner)
                    .collection("Announcement")
                    .document(announcement.id!!)
                    .update("reservation_counter", FieldValue.increment(1))
            }

    }

    companion object {
        private var bookingRepository: BookingRepository? = null
        val instance: BookingRepository
            get() = BookingRepository().also { bookingRepository = it }
    }
}