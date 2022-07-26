package com.example.boatbooking_1.model

class Boat {
    var builder: String? = null
    var model: String? = null
    var year: Int? = null
    var length: Int? = null
    var passengers: Int? = null
    var license: Boolean? = null

    constructor() {}

    constructor(
        builder: String?,
        model: String?,
        year: Int?,
        length: Int?,
        passengers: Int?,
        license: Boolean?
    ) {
        this.builder = builder
        this.model = model
        this.year = year
        this.length = length
        this.passengers = passengers
        this.license = license
    }

}
