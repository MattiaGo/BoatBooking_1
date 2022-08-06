package com.example.boatbooking_1.model

import java.util.*

class Booking(
    var id: String? = null,
    var announcement: Announcement? = null,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var services: ArrayList<BoatService>? = ArrayList(),
    var idOwner: String? = null,
    var total: Int = 0
) {

    override fun toString(): String {
        return "\nStart: ${startDate.toString()}\nEnd: ${endDate.toString()}\n${services.toString()}"
    }

}