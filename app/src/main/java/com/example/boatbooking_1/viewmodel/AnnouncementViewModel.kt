package com.example.boatbooking_1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.repository.AnnouncementRepository
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter

class AnnouncementViewModel : ViewModel() {
    private val repository: AnnouncementRepository = AnnouncementRepository.instance

    fun addAnnouncementToDatabase(announcement: Announcement, announcementID: String) {
        repository.addAnnouncementToDatabase(announcement,announcementID)
    }

    fun getAnnouncement() : LiveData<Announcement> {
        return repository.announcementLiveData
    }

    fun getOwnerAnnouncement(myAnnouncementList: ArrayList<Announcement>, myAnnouncementAdapter: MyAnnouncementAdapter){
        repository.getOwnerAnnouncement(myAnnouncementList, myAnnouncementAdapter)
    }

    fun updateAnnouncement(announcementID: String) {
        repository.updateAnnouncementOnDatabase(announcementID)
    }

    fun setAnnouncement(id: String?) {
        repository.setAnnouncementLiveData(id)
    }

    fun refreshAnnouncement(announcement: Announcement) {
        repository.refreshAnnouncement(announcement)
    }

    fun getBestHomeAnnouncements(myAnnouncementList: ArrayList<Announcement>, myAnnouncementAdapter: PublicAnnouncementAdapter){
        repository.getBestHomeAnnouncements(myAnnouncementList,myAnnouncementAdapter)
    }
}