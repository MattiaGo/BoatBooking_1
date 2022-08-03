package com.example.boatbooking_1.repository

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter

import com.example.boatbooking_1.utils.Util
import com.google.firebase.database.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AnnouncementRepository {
    private var _announcementLiveData = MutableLiveData<Announcement>()
    val announcementLiveData: LiveData<Announcement>
        get() = _announcementLiveData

    init {
        resetLiveData()
    }

    fun resetLiveData() {
        _announcementLiveData = MutableLiveData()
    }

    fun setAnnouncementLiveData(id: String?, context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Caricamento...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .document(id!!)
            .get()
            //Util.fDatabase.collection(Util.getUID()!!).whereEqualTo("id", id).get()
            .addOnSuccessListener { document ->
                val announcement = document.toObject(Announcement::class.java)
                Log.d("Firestore", "setAnnouncementLiveData()")
                val fDatabase = FirebaseFirestore.getInstance()

                _announcementLiveData.value = announcement
                Log.d(
                    "Firestore",
                    "LiveData ${_announcementLiveData.value.toString()}"
                )
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
//                        TODO("Not yet implemented")
//                    }
//
//                })

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

    }

    fun updateAnnouncementOnDatabase(announcementID: String) {
        val announcement = _announcementLiveData.value

        Log.d("Firestore", "Pre-updated: ${announcement.toString()}")

        val formatter = Util.sdf()
        val now = Date()
        val fileName = formatter.format(now)

        val newImagesURI = ArrayList<String>()

        announcement?.imageList!!.forEachIndexed() { i, image ->
            if (image.startsWith("content://")) {
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

//                    resetLiveData()
            }

//        Util.mDatabase.child("announcements").child(Util.getUID().toString())
//            .child(announcementUpdated!!.id.toString())
//            .setValue(announcementUpdated)
//            .addOnSuccessListener {
//                Log.d("Firebase", "onSuccess: Announcement updated!")
//            }
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


    fun getBestHomeAnnouncements(
        arrayList: ArrayList<Announcement>,
        myAnnouncementAdapter: PublicAnnouncementAdapter
    ) {
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

