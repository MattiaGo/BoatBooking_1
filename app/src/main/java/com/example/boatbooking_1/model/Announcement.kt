package com.example.boatbooking_1.model

data class Announcement(
    var id: String? = null,
    var id_owner: String? = null,
    var announce_name: String? = null,
    var boat: Boat? = null,
    var capt_needed: Boolean? = true,
    var licence_needed: Boolean? = true,
    var location: String? = null,
    var description: String? = null,
    var services: ArrayList<BoatService>? = null,
    var imageList: ArrayList<String>? = null,
    var available: Boolean = false
) {
    override fun toString(): String {
        return "\nBOAT:\n${boat}ANNOUNCEMENT:\n$announce_name\n$location\n"
    }

}