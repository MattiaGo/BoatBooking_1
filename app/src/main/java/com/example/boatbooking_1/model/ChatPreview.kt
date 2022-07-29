package com.example.boatbooking_1.model


class ChatPreview {
    var user: User? = null
    var lastMessage: String? = null
    var timestamp: Long? = null

    constructor() {}

    constructor(user: User?, lastMessage: String?, timestamp: Long?) {
        this.user = user
        this.lastMessage = lastMessage
        this.timestamp = timestamp
    }


}