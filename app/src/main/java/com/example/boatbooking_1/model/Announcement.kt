package com.example.boatbooking_1.model

import java.util.*
import kotlin.collections.ArrayList

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
    var price: Int? = null,
    var available: Boolean = false,
    var average_vote: Double = 0.00,
    var timestamp: com.google.firebase.Timestamp? = null
) {
    override fun toString(): String {
        return "\nBOAT:\n${boat}ANNOUNCEMENT:\n$announce_name" +
                "\n${
                    location.toString()
                        .uppercase(Locale.getDefault())
                }\n"
    }
}