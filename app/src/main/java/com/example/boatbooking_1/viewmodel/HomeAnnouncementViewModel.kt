package com.example.boatbooking_1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.firestore.ktx.toObject

class HomeAnnouncementViewModel : ViewModel() {

    fun getBestHomeAnnouncements(
        arrayList: java.util.ArrayList<Announcement>,
        myAnnouncementAdapter: PublicAnnouncementAdapter
    ) {
//        Util.fDatabase.collection("BoatAnnouncement")
//            .collectionGroup("Announcement")
//            .whereGreaterThanOrEqualTo("average_vote", 4.5)
//            .get()
//            .addOnSuccessListener { documents ->
//                documents.forEach { document ->
//                    val announcement = document.toObject(Announcement::class.java)
//                    arrayList.add(announcement)
//                }
//                myAnnouncementAdapter.notifyDataSetChanged()
//            }
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

    fun getPositionAnnouncement(
        myAnnouncementList: ArrayList<Announcement>,
        myAnnouncementAdapter: MyAnnouncementAdapter
    ) {
        Util.fDatabase.collection("BoatAnnouncement")
            .get().addOnSuccessListener {
                Log.d("Firestore", it.documents.toString())

                it.documents.forEach { snapshot ->
                   snapshot.reference.collection("Announcement")
                       .whereEqualTo("location", "Iseo")
                       .get()
                       .addOnSuccessListener {
                           Log.d("Firestore", it.toString())
                           it.forEach {
                               Log.d("Firestore", it.toObject(Announcement::class.java).toString())
                           }
                       }
                }
            }

            }

//                it.result.forEach {
//                    Log.d("Firestore", it.toString())
//                    Util.fDatabase.collection("Announcement")
//                        .get()
//                        .addOnCompleteListener { task ->
//                            task.result.documents.forEach {
//                                Log.d("Firestore", it.toObject(Announcement::class.java).toString())
//                                myAnnouncementList.add(
//                                    it.toObject(Announcement::class.java)!!
//                                )
//                            }
//                        }
//                }
    }