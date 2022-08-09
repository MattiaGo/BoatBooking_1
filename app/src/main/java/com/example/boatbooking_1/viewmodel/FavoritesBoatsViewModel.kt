package com.example.boatbooking_1.viewmodel

import android.util.Log
import android.widget.ImageButton
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.ui.FavoritesAnnouncementAdapter
import com.example.boatbooking_1.utils.Util

class FavoritesBoatsViewModel : ViewModel() {

    // TODO: Fix the visualization of like button

    fun getFavoritesBoats(
        arrayList: ArrayList<Announcement>,
        adapter: FavoritesAnnouncementAdapter
    ) {
        Util.fDatabase.collection("UsersFavorites")
            .document(Util.getUID()!!)
            .collection("Favorites")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    val announcement = document.toObject(Announcement::class.java)
                    arrayList.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
    }

    fun manageFavoritesBoat(announcement: Announcement, ib: ImageButton) {
        val uid = Util.getUID()!!

        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Favorites")
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

    private fun addBoatToFavorites(announcement: Announcement, uid: String){
        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Favorites")
            .document(announcement.id!!)
            .set(announcement)
            .addOnCompleteListener {
                Log.d("Booking", "OnComplete: Favorite ADDED!")
            }
            .addOnFailureListener {
                Log.d("Booking", "Error: $it")
            }
    }

    private fun removeBoatFromFavorites(announcement: Announcement, uid: String){
        Util.fDatabase.collection("UsersFavorites")
            .document(uid)
            .collection("Favorites")
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
