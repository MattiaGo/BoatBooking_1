package com.example.boatbooking_1.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.database.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


class AnnouncementRepository {
    private var _announcementLiveData = MutableLiveData<Announcement>()
    val announcementLiveData: LiveData<Announcement>
        get() = _announcementLiveData

    init {
        resetLiveData()
    }

    fun setAnnouncementLiveData(id: String?) {
        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .whereEqualTo("id", id)
            .get()
        //Util.fDatabase.collection(Util.getUID()!!).whereEqualTo("id", id).get()
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

    fun getOwnerAnnouncement(myAnnouncementList: ArrayList<Announcement>,myAnnouncementAdapter: MyAnnouncementAdapter){
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

    fun addAnnouncementToDatabase(announcement: Announcement , announcementID: String){
        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(announcementID)
            .set(announcement)
    }

    fun updateAnnouncementOnDatabase(announcementID: String) {
        val announcement = _announcementLiveData.value

        Log.d("Firestore", "Pre-updated: ${announcement.toString()}")
        // TODO: Firestore actions to update announcement data

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(announcementID)
            .update(
                "id", announcement?.id,
                "id_owner", announcement?.id_owner,
                "announce_name", announcement?.announce_name,
                "boat", announcement?.boat,
                "capt_needed", announcement?.capt_needed,
                "licence_needed", announcement?.licence_needed,
                "location", announcement?.location,
                "description", announcement?.description,
                "imageList", announcement?.imageList,
                "services", announcement?.services,
                "available", announcement?.available,
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


    fun getBestHomeAnnouncements(arrayList: ArrayList<Announcement>, myAnnouncementAdapter: PublicAnnouncementAdapter) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereGreaterThanOrEqualTo("average_vote", 4.5)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
               val announcement = document.toObject(Announcement::class.java)
                arrayList.add(announcement)
            }
            myAnnouncementAdapter.notifyDataSetChanged()
        }


    }
    /*.collection("Announcement")
    .whereGreaterThanOrEqualTo("average_vote", 4.5)
    .get()
    .addOnSuccessListener {
    }

     */

    companion object {
        private var announcementRepository: AnnouncementRepository? = null
        val instance: AnnouncementRepository
            get() = AnnouncementRepository().also { announcementRepository = it }
    }
}

