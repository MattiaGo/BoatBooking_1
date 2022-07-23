package com.example.boatbooking_1.model

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null

    // Firebase needs empty constructor
    constructor() {}

    constructor(name: String?, email: String?, uid: String?) {
        this.name = name
        this.email = email
        this.uid = uid
    }
}