package com.example.boatbooking_1.model

import java.util.*

data class Search(
    var startDate: Date? = null,
    var endDate: Date? = null,

    var lvYear: Int? = null,
    var hvYear: Int? = null,
    var lvLength: Int? = null,
    var hvLength: Int? = null,
    var lvPassengers: Int? = null,
    var hvPassengers: Int? = null,
    var lvBeds: Int? = null,
    var hvBeds: Int? = null,
    var lvCabins: Int? = null,
    var hvCabins: Int? = null,
    var lvBath: Int? = null,
    var hvBath: Int? = null,

    var licenceNeeded: Boolean? = null,
    var captainNeeded: Boolean? = null,

    var lvPrice: Int? = null,
    var hvPrice: Int? = null,
)