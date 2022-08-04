package com.example.boatbooking_1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.firestore.ktx.toObject

const val limit_of_query: Long = 4

class HomeAnnouncementViewModel : ViewModel() {

    fun getBestHomeAnnouncements(
        arrayList: java.util.ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereGreaterThan("average_vote",4)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
    }

    fun getMostRequestedAnnouncement(
        arrayList: java.util.ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereGreaterThan("average_vote",4)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
    }

    fun getLastAddedAnnouncement(
        arrayList: java.util.ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereGreaterThan("average_vote",4)
            .limit(limit_of_query)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
    }

    fun getPositionAnnouncement(
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter
    ) {


        Util.fDatabase.collectionGroup("Announcement")
            .whereEqualTo("location","LIVORNO")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
        }

    /* Util.fDatabase.collection("BoatAnnouncement")
            .get().addOnSuccessListener {
                it.documents.forEach { snapshot ->
                   snapshot.reference.collection("Announcement")
                       .whereEqualTo("location", "GENOVA")
                       .get()
                       .addOnSuccessListener {
                           it.forEach {
                               val announcement = it.toObject(Announcement::class.java)
                               Log.d("Firestore", announcement.toString())
                               myAnnouncementList.add(announcement)
                           }
                           adapter.notifyDataSetChanged()
                       }

                }
            }

         */
    }