package com.example.boatbooking_1.model

import com.google.firebase.Timestamp


class MyMessage {
    var message: String? = null
    var senderID: String? = null
    var timestamp: Long? = null

    constructor() {}

    constructor(message: String?, senderID: String?) {
        this.message = message
        this.senderID = senderID
        this.timestamp = Timestamp.now().seconds
    }
}
