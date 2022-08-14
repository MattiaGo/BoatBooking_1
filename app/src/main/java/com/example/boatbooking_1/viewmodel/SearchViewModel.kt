package com.example.boatbooking_1.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.model.Search
import com.example.boatbooking_1.adapter.SearchResultAnnouncementAdapter
import com.example.boatbooking_1.utils.Util
import me.moallemi.tools.daterange.date.rangeTo
import kotlin.collections.ArrayList

class SearchViewModel : ViewModel() {
    private var _searchData = Search()
    val searchData: Search
        get() = _searchData

    fun resetData() {
        _searchData = Search()
    }

    fun resetFilterParam() {
        val location = _searchData.location
        val startDate = _searchData.startDate
        val endDate = _searchData.endDate

        _searchData = Search()

        _searchData.location = location
        _searchData.startDate = startDate
        _searchData.endDate = endDate
    }

//TODO location
    fun searchAnnouncement(
    arrayList: ArrayList<Announcement>,
    searchParam: Search,
    adapter: SearchResultAnnouncementAdapter,
    remoteImageURIList: MutableList<String> ){
        Util.fDatabase.collectionGroup("Announcement")
            .whereEqualTo("location", searchParam.location!!)
            .whereEqualTo("available", true)
            .whereEqualTo("licence_needed", searchParam.licenceNeeded!!)
            .whereEqualTo("capt_needed", searchParam.captainNeeded!!)
            .whereGreaterThanOrEqualTo("price", searchParam.lvPrice!!)
            .whereLessThanOrEqualTo("price", searchParam.hvPrice!!)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEachIndexed { i, document ->
                    val announcement = document.toObject(Announcement::class.java)

                    val rangeInputDate = searchParam.startDate!!.rangeTo(searchParam.endDate!!)
                    var available = false
                    Util.fDatabase.collectionGroup("Booking")
                        .whereEqualTo("aid", announcement.id)
                        .get()
                        .addOnSuccessListener {
//                var availableUpdated = _available.value
                            if (it.isEmpty) {
                                available = it.isEmpty
                            } else {
                                available = true

                                loop@for (document in it) {
                                    val booking = document.toObject(Booking::class.java)
                                    val rangeDate = booking.startDate!!.rangeTo(booking.endDate!!)

                                    for (date in rangeDate) {
//                            Log.d("MyDebug", "for: $date}")
                                        if (date in rangeInputDate) {
//                                Log.d("MyDebug", "if: $date}")
                                            available = false
                                            break@loop
                                        }
                                    }
//                    Log.d("MyDebug", booking.toString())
                                }
                            }

                            if(available){
                                if (filterBoatInfo(announcement.boat!!, searchParam)) {
                                    arrayList.add(i, announcement)

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
                                }
                            }



                        }
                        .addOnFailureListener {
                            Log.d("Booking", "Error: $it")
                        }
                }
            }
            .addOnFailureListener {
                Log.d("Error", it.toString())
            }
    }

    private fun getImageForAnnouncement(
        imageName: String?,
        adapter: SearchResultAnnouncementAdapter,
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

    private fun filterBoatInfo(
        boat: Boat,
        searchParam: Search
    ): Boolean{
        return boat.year!! in searchParam.lvYear!!..searchParam.hvYear!! &&
                boat.length!! in searchParam.lvLength!!..searchParam.hvLength!! &&
                boat.passengers!! in searchParam.lvPassengers!!..searchParam.hvPassengers!! &&
                boat.beds!! in searchParam.lvBeds!!..searchParam.hvBeds!! &&
                boat.cabins!! in searchParam.lvCabins!!..searchParam.hvCabins!! &&
                boat.bathrooms!! in searchParam.lvBath!!..searchParam.hvBath!!
    }

    fun getLocationsFromDatabase(arrayList: ArrayList<String>){
        Util.fDatabase.collection("Locations")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { document ->
                    arrayList.add(document.get("location").toString())
                }
            }
    }
}