package com.example.boatbooking_1.model

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var location: String? = null
    var shipOwner: Boolean? = null

    // Firebase needs empty constructor
    constructor() {}

    constructor(
        name: String?,
        email: String?,
        uid: String?,
        location: String?,
        shipOwner: Boolean?
    ) {
        this.name = name
        this.email = email
        this.uid = uid
        this.location = location
        this.shipOwner = shipOwner
    }

}