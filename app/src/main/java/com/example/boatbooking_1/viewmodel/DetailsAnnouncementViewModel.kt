package com.example.boatbooking_1.viewmodel

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.ImageButton
import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.ui.*
import com.example.boatbooking_1.utils.Util
import com.google.firebase.firestore.ktx.toObject

class DetailsAnnouncementViewModel : ViewModel() {
    fun getImageForAnnouncement(
        imagesName: ArrayList<String>,
        adapter: PublicImageAdapter,
        remoteImageURIList: ArrayList<String>
    ) {
//              Log.d("Firestore", serviceListString.toString())
        for (image in imagesName) {
//                val localFile = File.createTempFile("temp-image$i", ".jpg")

            var downloadUri: Uri
//                var remoteImages: ArrayList<String> = ArrayList()

            Util.fStorage.reference.child("/images/$image")
                .downloadUrl
                .addOnCompleteListener {
                    // Got the download URL
                    downloadUri = it.result
                    Log.d("Adapter", "$downloadUri")
//                                        val generatedFilePath = downloadUri.toString()
                    if (!remoteImageURIList.contains(downloadUri.toString())) {
                        remoteImageURIList.add(downloadUri.toString())
                    }
                    //remoteImageList.add(image) // Name of image on Storage

                    Log.d("Adapter", "$remoteImageURIList")
                    /// The string (file link) that you need
//                    if (progressDialog.isShowing) {
//                        progressDialog.dismiss()
//                    }


//                    if (progressDialog.isShowing) {
//                        progressDialog.dismiss()
//                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
//                    if (progressDialog.isShowing) {
//                        progressDialog.dismiss()
//                    }
                    Log.d("Adapter", "Error: $it")
                }
        }
        //}
        //}
    }

    /*
    fun getImageForAnnouncement(announcement_id: String, list: ArrayList<String>, adapter: ImageAdapter , progressDialog: ProgressDialog, remoteImageURIList: ArrayList<String>, remoteImageList: ArrayList<String>) {
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

     */

    fun getServiceForAnnouncement(
        announcement_id: String,
        list: ArrayList<BoatService>,
        adapter: PublicServiceAdapter
    ) {
        Util.fDatabase.collectionGroup("BoatAnnouncement")
            .whereEqualTo("id", announcement_id)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    val services = announcement.services
                    val serviceListMap = services as ArrayList<HashMap<String, String>>

                    when (services) {
                        null -> {}
                        else -> {
                            for (service in serviceListMap) {
                                val boatService = BoatService(
                                    service["name"]!!,
                                    service["price"]!!
                                )
                                list.add(boatService)
                            }
                            adapter.notifyDataSetChanged()
                        }

                    }

                }
            }
    }

    fun checkIfFavorite(id: String, imgButton: ImageButton) {
        Util.fDatabase.collection("UsersFavorites")
            .document(Util.getUID()!!)
            .collection("Favorites")
            .document(id)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    imgButton.setImageResource(R.drawable.ic_favorite)
                } else {
                    imgButton.setImageResource(R.drawable.ic_favorite_border)
                }
            }
            .addOnFailureListener {
                Log.d("Error", it.toString())
            }
    }
}