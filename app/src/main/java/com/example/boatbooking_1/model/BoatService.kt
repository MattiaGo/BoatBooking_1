package com.example.boatbooking_1.model

class BoatService(var name: String? = null, var price: String? = null) {

    override fun toString(): String {
        return "BoatService: ( $name , $price )"
    }
}