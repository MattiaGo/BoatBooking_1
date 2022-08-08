package com.example.boatbooking_1.model

import android.widget.ImageView
import com.bumptech.glide.Glide

data class User(var name: String? = null, var email: String? = null, var uid: String? = null, var location: String? = null, var image: String? = null, var shipOwner: Boolean = false) {

    // Firebase needs empty constructor
     //constructor() : this() {}

    /*
    constructor(
        name: String,
        email: String,
        image: String,
        isShipOwner: Boolean
    ) :
        this(name)
        this(email)
        this(image)
        this(isShipOwner)
*/
    fun loadImage(view: ImageView, image: String) {
        Glide.with(view.context).load(image).into(view)
    }

    override fun toString(): String {
        return "$name@$uid ($shipOwner)"
    }
}

/*data class User(val name: String? = null, val email: String? = null, val image: String? = null, val isShipOwner: Boolean? = false) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.

    var image: String
*/
