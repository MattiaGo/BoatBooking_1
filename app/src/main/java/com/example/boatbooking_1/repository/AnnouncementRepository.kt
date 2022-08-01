package com.example.boatbooking_1.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.MyMessage
import com.example.boatbooking_1.utils.Util
import com.google.firebase.database.*
import com.google.protobuf.Value

class AnnouncementRepository {
    private var _announcementLiveData = MutableLiveData<Announcement>()
    val announcementLiveData: LiveData<Announcement>
        get() = _announcementLiveData

    init {
        _announcementLiveData = MutableLiveData()
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

        Log.d("LiveData", _announcementLiveData.value.toString())
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
        val announcementUpdated = _announcementLiveData.value

        // TODO: Firestore actions to update announcement data

//        Util.mDatabase.child("announcements").child(Util.getUID().toString())
//            .child(announcementUpdated!!.id.toString())
//            .setValue(announcementUpdated)
//            .addOnSuccessListener {
//                Log.d("Firebase", "onSuccess: Announcement updated!")
//            }
    }

    companion object {
        private var announcementRepository: AnnouncementRepository? = null
        val instance: AnnouncementRepository
            get() = AnnouncementRepository().also { announcementRepository = it }
    }
}

