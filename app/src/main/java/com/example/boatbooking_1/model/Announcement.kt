package com.example.boatbooking_1.model

data class Announcement(
    var boat: Boat? = null,
    var name: String? = null,
    var id: String? = null,
    var location: String? = null,
    var description: String? = null,
    var services: ArrayList<BoatService>? = null,
    var imageList: ArrayList<String>? = null,
    var available: Boolean? = null
) {
    override fun toString(): String {
        return "\nBOAT:\n${boat}ANNOUNCEMENT:\n$name\n$location\n"
    }

}