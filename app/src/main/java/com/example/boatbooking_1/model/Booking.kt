package com.example.boatbooking_1.model

import com.example.boatbooking_1.utils.Util
import java.util.*

class Booking(
    var id: String? = null,
    var announcement: Announcement? = null,
    var AID: String? = null,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var services: ArrayList<BoatService>? = ArrayList(),
    var idShipOwner: String? = null,
    var total: Int = 0
) {

    override fun toString(): String {
//        return "\nStart: ${startDate.toString()}\nEnd: ${endDate.toString()}\n${services.toString()}"
        return "Data inizio: ${Util.sdfBooking().format(startDate)}\n" +
                "Data fine: ${Util.sdfBooking().format(endDate)}\n" +
                "Luogo: ${announcement!!.location}\n" +
                "Totale: $total €"
    }

}