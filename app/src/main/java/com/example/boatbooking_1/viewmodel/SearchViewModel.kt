package com.example.boatbooking_1.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.adapter.SearchResultAnnouncementAdapter
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Boat
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.model.Search
import com.example.boatbooking_1.utils.Util
import me.moallemi.tools.daterange.date.rangeTo

class SearchViewModel : ViewModel() {
    private var _searchData = Search()
    val searchData: Search
        get() = _searchData

    var locationList = MutableLiveData<ArrayList<String>>()

    init {
        locationList.value = arrayListOf()
    }

    fun resetData() {
        _searchData = Search()
    }

    fun resetLocationList() {
        locationList.value = arrayListOf()
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

    fun searchAnnouncement(
        arrayList: ArrayList<Announcement>,
        searchParam: Search,
        adapter: SearchResultAnnouncementAdapter,
        remoteImageURIList: MutableList<String>
    ) {
        Util.fDatabase.collectionGroup("Announcement")
            .whereEqualTo("location", searchParam.location!!)
            .whereEqualTo("available", true)
            .whereEqualTo("licence_needed", searchParam.licenceNeeded!!)
            .whereEqualTo("capt_needed", searchParam.captainNeeded!!)
//            .whereGreaterThanOrEqualTo("price", searchParam.lvPrice!!)
//            .whereLessThanOrEqualTo("price", searchParam.hvPrice!!)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEachIndexed { i, document ->
                    val announcement = document.toObject(Announcement::class.java)

//                    Log.d("Search", announcement.toString())

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

                                loop@ for (doc in it) {
                                    val booking = doc.toObject(Booking::class.java)
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

                            if (available) {
                                if (filterBoatInfo(announcement.boat!!, searchParam) && checkPrice(
                                        announcement,
                                        searchParam
                                    )
                                ) {
                                    arrayList.add(i, announcement)
                                    Log.d("Search", announcement.toString())

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

    private fun checkPrice(announcement: Announcement, searchParam: Search): Boolean {
        return announcement.price!! in searchParam.lvPrice!!..searchParam.hvPrice!!
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
    ): Boolean {
        return boat.year!! in searchParam.lvYear!!..searchParam.hvYear!! &&
                boat.length!! in searchParam.lvLength!!..searchParam.hvLength!! &&
                boat.passengers!! in searchParam.lvPassengers!!..searchParam.hvPassengers!! &&
                boat.beds!! in searchParam.lvBeds!!..searchParam.hvBeds!! &&
                boat.cabins!! in searchParam.lvCabins!!..searchParam.hvCabins!! &&
                boat.bathrooms!! in searchParam.lvBath!!..searchParam.hvBath!!
    }

    fun getLocationsFromDatabase(adapter: ArrayAdapter<String>) {

        Util.fDatabase.collection("Locations")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("Search", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val al = arrayListOf<String>()

                if (snapshot != null) {
                    for (it in snapshot) {
                        Log.d("Search", it.reference.id)
                        al.add(it.reference.id)
                        //adapter.notifyDataSetChanged()
                    }
                    //adapter.notifyDataSetChanged()
                }

                locationList.value = al
                adapter.notifyDataSetChanged()
            }
    }
}