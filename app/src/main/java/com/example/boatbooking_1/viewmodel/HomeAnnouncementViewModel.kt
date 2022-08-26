package com.example.boatbooking_1.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.adapter.PublicAnnouncementAdapter
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.utils.Util
import com.google.firebase.firestore.Query

const val limit_of_query: Long = 4

class HomeAnnouncementViewModel : ViewModel() {

    fun getMostRequestedAnnouncement(
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter,
        remoteImageURIList: MutableList<String>
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .orderBy("reservation_counter", Query.Direction.DESCENDING)
            .limit(limit_of_query)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEachIndexed { i, document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(i, announcement)

                    Log.d("Home", announcement.imageList.toString())
                    Log.d("Home", announcement.imageList!!.isNotEmpty().toString())

                    if (announcement.imageList!!.isNotEmpty()) {
                        getImageForAnnouncement(
                            announcement.imageList?.get(0),
                            adapter,
                            remoteImageURIList,
                            i
                        )
                    } else {
                        remoteImageURIList.add("default")
                        adapter.notifyDataSetChanged()
                    }

                    Log.d("Home", "RemoteURIList: ${remoteImageURIList.toString()}")
                }
//                Log.d("Firestore", "announcementList: $arrayList")
            }
    }

    private fun getImageForAnnouncement(
        imageName: String?,
        adapter: PublicAnnouncementAdapter,
        remoteImageURIList: MutableList<String>,
        position: Int
    ) {
        var downloadUri: Uri

        remoteImageURIList.add(position, "")

        Util.fStorage.reference.child("/images/$imageName")
            .downloadUrl
            .addOnSuccessListener {
                // Got the download URL
                downloadUri = it
                remoteImageURIList.add(position, downloadUri.toString())
                adapter.notifyDataSetChanged()
//                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("HomeImage", "Error: $it")
            }
    }


    fun getLastAddedAnnouncement(
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter,
        remoteImageURIList: MutableList<String>
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .orderBy("timestamp")
            .limit(limit_of_query)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEachIndexed { i, document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(i, announcement)

                    Log.d("Home", announcement.imageList.toString())
                    Log.d("Home", announcement.imageList!!.isNotEmpty().toString())

                    if (announcement.imageList!!.isNotEmpty()) {
                        getImageForAnnouncement(
                            announcement.imageList?.get(0),
                            adapter,
                            remoteImageURIList,
                            i
                        )
                    } else {
                        remoteImageURIList.add("default")
                        adapter.notifyDataSetChanged()
                    }

                    Log.d("Home", "LastAdded: ${remoteImageURIList}")
                }
//                Log.d("Firestore", "announcementList: $arrayList")
            }
    }

    fun getLastSeenAnnouncement(
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter,
        remoteImageURIList: MutableList<String>
    ) {
        Util.fDatabase.collection("UsersLastSeen")
            .document(Util.getUID()!!)
            .collection("LastSeen")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEachIndexed { i, document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(i, announcement)

                    Log.d("Home", announcement.imageList.toString())
                    Log.d("Home", announcement.imageList!!.isNotEmpty().toString())

                    if (announcement.imageList!!.isNotEmpty()) {
                        getImageForAnnouncement(
                            announcement.imageList?.get(0),
                            adapter,
                            remoteImageURIList,
                            i
                        )
                    } else {
                        remoteImageURIList.add("default")
                        adapter.notifyDataSetChanged()
                    }

                    Log.d("Home", "RemoteURIList: ${remoteImageURIList.toString()}")
                }
//                Log.d("Firestore", "announcementList: $arrayList")
            }
    }
}
