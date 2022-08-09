package com.example.boatbooking_1.viewmodel

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.ImageAdapter
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicImageAdapter
import com.example.boatbooking_1.utils.Util
import com.google.firebase.firestore.ktx.toObject

const val limit_of_query: Long = 4

class HomeAnnouncementViewModel : ViewModel() {

    fun getBestHomeAnnouncements(
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereGreaterThan("average_vote", 4)
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
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter,
        remoteImageURIList: MutableList<String>
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereLessThan("average_vote", 4)
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
            .orderBy("timestamp").limit(limit_of_query)
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


    fun getPositionAnnouncement(
        arrayList: ArrayList<Announcement>,
        adapter: PublicAnnouncementAdapter
    ) {


        Util.fDatabase.collectionGroup("Announcement")
            .whereEqualTo("location", "LIVORNO")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
    }
}

fun getRemoteImageforAnnouncement(
    announcement_id: String,
    list: ArrayList<String>,
    adapter: ImageAdapter,
    progressDialog: ProgressDialog,
    remoteImageURIList: ArrayList<String>,
    remoteImageList: ArrayList<String>
) {
    Util.fDatabase.collectionGroup("Announcement")
        .whereEqualTo("id", announcement_id)
        .get()
        .addOnSuccessListener { documents ->
            documents.forEach { document ->
                val announcement = document.toObject(Announcement::class.java)
                val images = announcement.imageList

                Log.d("immagini", images.toString())
                val imageListMap = images as ArrayList<String>

                when (images) {
                    null -> {}
                    else -> {
                        if (imageListMap.isEmpty()) {
                            if (progressDialog.isShowing) {
                                progressDialog.dismiss()
                            }
                        }
//                Log.d("Firestore", serviceListString.toString())
                        for (image in imageListMap) {
//                val localFile = File.createTempFile("temp-image$i", ".jpg")

                            var downloadUri: Uri
//                var remoteImages: ArrayList<String> = ArrayList()

                            Util.fStorage.reference.child("/images/$image")
                                .downloadUrl
                                .addOnCompleteListener {
                                    // Got the download URL
                                    if (it.isSuccessful) {
                                        downloadUri = it.result
                                        Log.d("Adapter", "$downloadUri")
//                                        val generatedFilePath = downloadUri.toString()
                                        remoteImageURIList.add(downloadUri.toString())
                                        remoteImageList.add(image) // Name of image on Storage
                                        adapter.notifyDataSetChanged()
                                        Log.d("Adapter", "$remoteImageURIList")
                                        /// The string (file link) that you need
                                        if (progressDialog.isShowing) {
                                            progressDialog.dismiss()
                                        }
                                    }

                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                }
                                .addOnFailureListener {
                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                    Log.d("Adapter", "Error: $it")
                                }
                        }
                    }
                }
            }
        }
        .addOnFailureListener {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
}