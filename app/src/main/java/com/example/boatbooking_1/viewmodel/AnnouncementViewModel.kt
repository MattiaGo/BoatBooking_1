package com.example.boatbooking_1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.repository.AnnouncementRepository

class AnnouncementViewModel : ViewModel() {
    private val repository: AnnouncementRepository = AnnouncementRepository.instance

    fun addAnnouncementToDatabase(new: Announcement) {
        repository.addAnnouncementToDatabase(new)
    }

    fun getAnnouncement() : LiveData<Announcement> {
        return repository.announcementLiveData
    }

    fun updateAnnouncement() {
        repository.updateAnnouncementOnDatabase()
    }

    fun setAnnouncement(id: String?) {
        repository.setAnnouncementLiveData(id)
    }
}