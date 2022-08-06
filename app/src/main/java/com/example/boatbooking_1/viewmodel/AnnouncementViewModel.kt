package com.example.boatbooking_1.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.repository.AnnouncementRepository
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.utils.Util

class AnnouncementViewModel : ViewModel() {
    private val repository: AnnouncementRepository = AnnouncementRepository.instance

    fun addAnnouncementToDatabase(announcement: Announcement, announcementID: String, context: Context) {
        repository.addAnnouncementToDatabase(announcement, announcementID, context)
    }

    fun getAnnouncement(): LiveData<Announcement> {
        return repository.announcementLiveData
    }

    fun updateAnnouncement(announcementID: String) {
        repository.updateAnnouncementOnDatabase(announcementID)
    }

    fun setAnnouncement(id: String?, context: Context) {
        Log.d("Firestore", "setAnnouncement() ViewModel")
        repository.setAnnouncementLiveData(id, context)
    }

    fun refreshAnnouncement(announcement: Announcement) {
        repository.refreshAnnouncement(announcement)
    }

    fun getOwnerAnnouncement(
        myAnnouncementList: ArrayList<Announcement>,
        myAnnouncementAdapter: MyAnnouncementAdapter
    ) {
        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot) {
                    val announcement = document.toObject(Announcement::class.java)
                    myAnnouncementList.add(announcement)
//                    Log.d("Firestore", announcement.toString())
                }
                myAnnouncementAdapter.notifyDataSetChanged()
            }
    }

    fun reset() {
        repository.resetLiveData()
    }
}