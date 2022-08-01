package com.example.boatbooking_1.model

data class Announcement(
    var boat: Boat? = null,
//    val boat: Boat?
//        get() = _boat
    var name: String? = null,
//    val name: String?
//        get() = _name
    var id: String? = null,
//    val id: String?
//        get() = _id
    var location: String? = null,
//    val location: String?
//        get() = _location
    var description: String? = null,
    var services: ArrayList<BoatService>? = null,
    var imageList: ArrayList<String>? = null,
    var available: Boolean? = null
) {
    override fun toString(): String {
        return "\nBOAT:\n${boat}\n$name\n$location\n"
    }

}