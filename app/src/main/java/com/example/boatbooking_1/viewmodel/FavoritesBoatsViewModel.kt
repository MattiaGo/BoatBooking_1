package com.example.boatbooking_1.viewmodel

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.FavoritesAnnouncementAdapter
import com.example.boatbooking_1.ui.ImageAdapter
import com.example.boatbooking_1.ui.MyAnnouncementAdapter
import com.example.boatbooking_1.ui.PublicAnnouncementAdapter
import com.example.boatbooking_1.utils.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.toObject

class FavoritesBoatsViewModel : ViewModel() {

    fun getFavoritesBoats(
        arrayList: java.util.ArrayList<Announcement>,
        adapter: FavoritesAnnouncementAdapter
    ) {
        Util.fDatabase.collection("UsersFavorites")
            .document(Util.getUID()!!)
            .collection("Announcement")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
    }

    fun checkFavoriteStatus(announcement: Announcement): Boolean {
        val uid = Util.getUID()!!

        var exist: Boolean = false

        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Announcement")
            .document(announcement.id!!)
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    exist = true
                }
                else{
                    exist = false
                }
            }
            .addOnFailureListener {
                Log.d("Error", it.toString())
            }

        return exist
    }

    fun manageFavoritesBoat(announcement: Announcement, ib: ImageButton) {
        val uid = Util.getUID()!!

        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Announcement")
            .document(announcement.id!!)
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    removeBoatFromFavorites(announcement,uid)
                    ib.setImageResource(R.drawable.ic_favorite_border)
                }
                else{
                    addBoatToFavorites(announcement,uid)
                    ib.setImageResource(R.drawable.ic_favorite)
                }
            }
            .addOnFailureListener {
                Log.d("Error", it.toString())
            }
    }

    fun addBoatToFavorites(announcement: Announcement, uid: String){
        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Announcement")
            .document(announcement.id!!)
            .set(announcement)
            .addOnCompleteListener {
                Log.d("Booking", "OnComplete: Favorite ADDED!")
            }
            .addOnFailureListener {
                Log.d("Booking", "Error: $it")
            }
    }

    fun removeBoatFromFavorites(announcement: Announcement, uid: String){
        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Announcement")
            .document(announcement.id!!)
            .delete()
            .addOnCompleteListener {
                Log.d("Booking", "OnComplete: Favorite REMOVED!")
            }
            .addOnFailureListener {
                Log.d("Booking", "Error: $it")
            }
    }
}
