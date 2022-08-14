package com.example.boatbooking_1.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.repository.BookingRepository
import com.example.boatbooking_1.adapter.MyBookingAdapter
import java.util.*
import kotlin.collections.ArrayList

class BookingViewModel : ViewModel() {
    private val repository: BookingRepository = BookingRepository.instance

    fun getBooking() : LiveData<Booking> {
        return repository.bookingLiveData
    }

    fun getAvailability() : LiveData<Boolean> {
        return repository.available
    }

    fun resetAvailability() {
        repository.resetAvailability()
    }

    fun getUserBookingList(myBookingList: ArrayList<Booking>, myBookingAdapter: MyBookingAdapter) {
        repository.getUserBookingList(myBookingList, myBookingAdapter)
    }

    fun setBookingDate(start: Date, end: Date) {
        repository.setBookingDate(start, end)
    }

    fun setBasePrice(price: Int){
        repository.setBasePrice(price)
    }

    fun updateBookingTotal(addPrice: Int) {
        repository.updateBookingTotal(addPrice)
    }

    fun updateBoatServiceList(service: BoatService, toAdd: Boolean) {
        repository.updateBoatServiceList(service, toAdd)
    }

    fun addBookingOnDatabase(AID: String) {
        repository.addBookingOnDatabase(AID)
    }

    fun isPeriodAvailable(startDate: Date, endDate: Date, id: String, context: Context) {
        return repository.isPeriodAvailable(startDate, endDate, id, context)
    }

    fun incrementReservationCounter(announcement: Announcement){
        repository.incrementReservationCounter(announcement)
    }
}