package com.example.boatbooking_1.model

class ChatPreview {
    var user: User? = null
    var lastMessage: String? = null

    constructor() {}

    constructor(user: User?, lastMessage: String?) {
        this.user = user
        this.lastMessage = lastMessage
    }


}