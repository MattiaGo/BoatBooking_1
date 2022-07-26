package com.example.boatbooking_1.model

import java.time.Period

class Announcement {
    lateinit var boat: Boat // Dependency injection
    var name: String? = null
    var ownerID: String? = null
    var location: String? = null
    var price: Int? = null
    var imageList: MutableList<String>? = null
    var available: Boolean? = null

    constructor() {}

    constructor(
        name: String?,
        ownerID: String?,
        location: String?,
        price: Int?,
        imageList: MutableList<String>?,
        available: Boolean?
    ) {
        this.name = name
        this.ownerID = ownerID
        this.location = location
        this.price = price
        this.imageList = imageList
        this.available = available
    }
}