package com.example.boatbooking_1.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.MyMessage
import com.example.boatbooking_1.utils.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.protobuf.Value

class AnnouncementRepository {
    private var _announcementLiveData = MutableLiveData<Announcement>()
    val announcementLiveData: LiveData<Announcement>
        get() = _announcementLiveData

    init {
        resetLiveData()
    }

    fun setAnnouncementLiveData(id: String?) {
        Util.fDatabase.collection(Util.getUID()!!).whereEqualTo("id", id).get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot) {
                    val announcement = document.toObject(Announcement::class.java)
                    _announcementLiveData.value = announcement
//                    Log.d("Firestore", announcement.toString())
                }
            }

        Log.d("LiveData", _announcementLiveData.value.toString())

        //            Util.mDatabase.child("announcements")
//                .child(Util.getUID().toString())
//                .child(id!!)
//                .addListenerForSingleValueEvent(object: ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val announcement = snapshot.getValue(Announcement::class.java)
//                        _announcementLiveData.value = announcement
//                        Log.d("Firebase", announcement.toString())
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                })
    }

    fun addAnnouncementToDatabase(new: Announcement) {
//        Util.mDatabase.child("announcements").child(Util.getUID().toString())
//            .child(new.id.toString())
//            .setValue(new).addOnSuccessListener {
//                Log.d("Firebase", "onSuccess: Announcement added!")
//            }
    }

//    private fun updateMutableLiveData(new: Announcement) {
//    }

    fun updateAnnouncementOnDatabase() {
        val announcement = _announcementLiveData.value

        Log.d("Firestore", "Pre-updated: ${announcement.toString()}")
        // TODO: Firestore actions to update announcement data

        Util.fDatabase.collection(Util.getUID()!!)
            .document(announcement!!.id!!)
            .update(
                "id", announcement.id,
                "announce_name", announcement.announce_name,
                "boat", announcement.boat,
                "capt_needed", announcement.capt_needed,
                "licence_needed", announcement.licence_needed,
                "location", announcement.location,
                "description", announcement.description,
                "imageList", announcement.imageList,
                "services", announcement.services,
                "available", announcement.available,
            )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Firestore", "Updated")
                } else {
                    Log.d("Firestore", "Error")
                }

                resetLiveData()
            }

//        Util.mDatabase.child("announcements").child(Util.getUID().toString())
//            .child(announcementUpdated!!.id.toString())
//            .setValue(announcementUpdated)
//            .addOnSuccessListener {
//                Log.d("Firebase", "onSuccess: Announcement updated!")
//            }
    }

    private fun resetLiveData() {
        _announcementLiveData = MutableLiveData()
    }

    fun refreshAnnouncement(announcement: Announcement) {
        _announcementLiveData.value = announcement
    }

    fun changeAvailability(available: Boolean,id: String){
        Util.fDatabase.collection(Util.getUID()!!)
            .document(id!!)
            .update(
                "available", available,
            )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Firestore", "Updated")
                } else {
                    Log.d("Firestore", "Error")
                }
                resetLiveData()
            }
    }

    companion object {
        private var announcementRepository: AnnouncementRepository? = null
        val instance: AnnouncementRepository
            get() = AnnouncementRepository().also { announcementRepository = it }
    }
}

