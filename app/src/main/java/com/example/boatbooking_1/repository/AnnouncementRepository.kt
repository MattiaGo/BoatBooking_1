package com.example.boatbooking_1.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.utils.Util
import com.google.firebase.database.*

class AnnouncementRepository {
    private val _announcementList = ArrayList<Announcement>()
    private var _announcementLiveData = MutableLiveData<List<Announcement>>()
    val announcementLiveData: LiveData<List<Announcement>>
        get() = _announcementLiveData

    init {
        Util.mDatabase.child("announcements")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val announcement = snapshot.getValue(
                            Announcement::class.java
                        )
                        _announcementList.add(announcement!!)
                    }
                    _announcementLiveData.value = _announcementList
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Firebase", databaseError.toString())
                }
            })
    }

    fun addAnnouncementToDatabase(new: Announcement) {
        Util.mDatabase.child("announcements").child(Util.getUID().toString()).push()
            .setValue(new).addOnSuccessListener {
                updateMutableLiveData(new)
            }

    }

    private fun updateMutableLiveData(new: Announcement) {
        _announcementList.add(new)
        _announcementLiveData.value = _announcementList
    }

    fun updateAnnouncementOnDatabase(edit: Announcement) {
//        Util.mDatabase.child("announcements").child(Util.getUID().toString())
//            .setValue(edit).addOnSuccessListener {
//                Log.i("Firebase", "Announcement updated!")
//            }
    }

    companion object {
        private var announcementRepository: AnnouncementRepository? = null
        val instance: AnnouncementRepository
            get() = AnnouncementRepository().also { announcementRepository = it }
    }
}

