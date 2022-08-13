package com.example.boatbooking_1.repository

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Booking

import com.example.boatbooking_1.utils.Util

import com.google.firebase.firestore.FirebaseFirestore
import me.moallemi.tools.daterange.date.rangeTo
import java.util.*
import kotlin.collections.ArrayList

class AnnouncementRepository {
    private var _announcementLiveData = MutableLiveData<Announcement>()
    val announcementLiveData: LiveData<Announcement>
        get() = _announcementLiveData

    init {
//        resetLiveData()
    }

    fun resetLiveData() {
        _announcementLiveData = MutableLiveData()
    }

    fun setAnnouncementLiveData(id: String?, context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Caricamento...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Util.fDatabase.collectionGroup("Announcement")
            .whereEqualTo("id", id!!)
            .limit(1)
            .get()
            .addOnSuccessListener {
                val announcement = it.documents.first().toObject(Announcement::class.java)
//                Log.d("Firestore", "setAnnouncementLiveData()")
//                val fDatabase = FirebaseFirestore.getInstance()

                _announcementLiveData.value = announcement
//                Log.d(
//                    "Firestore",
//                    "LiveData ${_announcementLiveData.value.toString()}"
//                )
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }.addOnFailureListener {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }
    }


//        Log.d("Firestore", _announcementLiveData.value.toString())

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
//                    }
//
//                })

    fun addAnnouncementToDatabase(
        announcement: Announcement,
        announcementID: String,
        context: Context
    ) {
        val formatter = Util.sdf()
        val now = Date()
        val fileName = formatter.format(now)

        val newImagesURI = ArrayList<String>()

        announcement.imageList!!.forEachIndexed() { i, image ->
            val reference = Util.fStorage.getReference("images/$fileName" + "_$i")

            newImagesURI.add("${fileName}_$i")
            Log.d("Storage", "$i | ${newImagesURI[i]}")

            reference.putFile(Uri.parse(image))
                .addOnSuccessListener {
//                    Log.d("Storage", "Image uploaded ${Uri.parse(image)}")
//                    Log.d("Storage", announcement.imageList!![i])
                }
                .addOnFailureListener {
                    Log.d("Storage", "Error: $it")
                }
        }

        Log.d("Storage", newImagesURI.toString())

        announcement.imageList = newImagesURI

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(announcementID)
            .set(announcement)
            .addOnCompleteListener {
                Toast.makeText(
                    context,
                    "Annuncio aggiunto con successo!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        saveLocationToDatabase(announcement.location!!)

    }

    fun updateAnnouncementOnDatabase(announcementID: String) {
        val announcement = _announcementLiveData.value

//        Log.d("Firestore", "Pre-updated: ${announcement.toString()}")

        val formatter = Util.sdf()
        val now = Date()
        val fileName = formatter.format(now)

        val newImagesURI = ArrayList<String>()

        announcement?.imageList!!.forEachIndexed() { i, image ->
            if (image.startsWith("content://")) {
                val storageReference = Util.fStorage.getReference("images/$fileName" + "_$i")

                newImagesURI.add("${fileName}_$i")
                Log.d("Storage", "$i | ${newImagesURI[i]}")

                storageReference.putFile(Uri.parse(image))
                    .addOnSuccessListener {
//                    Log.d("Storage", "Image uploaded ${Uri.parse(image)}")
//                    Log.d("Storage", announcement.imageList!![i])
                    }
                    .addOnFailureListener {
                        Log.d("Storage", "Error: $it")
                    }
            } else {
                newImagesURI.add(image)
            }
        }

        Log.d("Storage", newImagesURI.toString())

        announcement.imageList = newImagesURI

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(announcementID)
            .update(
                "id", announcement.id,
                "id_owner", announcement.id_owner,
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
                    saveLocationToDatabase(announcement.location!!)
                    Log.d("Firestore", "Updated")
                } else {
                    Log.d("Firestore", "Error")
                }
                    resetLiveData()
            }

    }

    fun deleteAnnouncementOnDatabase(
        announcement: Announcement,
        context: Context
    ){
        val formatter = Util.sdf()
        val now = Date()
        val fileName = formatter.format(now)

        val newImagesURI = ArrayList<String>()

        announcement.imageList!!.forEach() { image ->
            val reference = Util.fStorage.getReference("images/$image").delete()
        }

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(announcement.id!!)
            .delete()
            .addOnSuccessListener {
                // Remove all bookings related to the announcement removed
                Util.fDatabase.collectionGroup("Booking")
                    .whereEqualTo("aid", announcement.id!!)
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            document.reference.delete()
                        }
                    }
            }
            .addOnFailureListener {
                Log.d("Firestore", "OnFailure: Announcement#${announcement.id!!} not removed!")
            }
    }

    fun refreshAnnouncement(announcement: Announcement) {
        _announcementLiveData.value = announcement
    }

    fun changeAvailability(available: Boolean, id: String) {
        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(id)
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

    fun saveLocationToDatabase(location: String){
        Util.fDatabase.collection("Locations")
            .document(location.toUpperCase())
            .set(
                hashMapOf(
                    "location" to location.toUpperCase()
                )
            )
    }

    companion object {
        private var announcementRepository: AnnouncementRepository? = null
        val instance: AnnouncementRepository
            get() = AnnouncementRepository().also { announcementRepository = it }
    }
}

