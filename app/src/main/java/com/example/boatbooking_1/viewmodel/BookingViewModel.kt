package com.example.boatbooking_1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.repository.AnnouncementRepository
import com.example.boatbooking_1.repository.BookingRepository
import com.example.boatbooking_1.ui.MyBookingAdapter
import java.util.*
import kotlin.collections.ArrayList

class BookingViewModel : ViewModel() {
    private val repository: BookingRepository = BookingRepository.instance

    fun getBooking() : LiveData<Booking> {
        return repository.bookingLiveData
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

    fun addBookingOnDatabase() {
        repository.addBookingOnDatabase()
    }
}