package com.example.boatbooking_1.model

import java.time.Period

class Announcement(// Dependency injection
    val boat: Boat,
    var name: String?,
    var ownerID: String?,
    var location: String?,
    var description: String?,
    var services: ArrayList<BoatService>?,
    var imageList: ArrayList<String>?,
    var available: Boolean?
) {

}