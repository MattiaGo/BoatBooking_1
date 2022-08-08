package com.example.boatbooking_1.model

import android.widget.ImageView
import com.bumptech.glide.Glide

data class User(var name: String? = null, var email: String? = null, var uid: String? = null, var location: String? = null, var image: String? = null, var shipOwner: Boolean = false) {

    // Firebase needs empty constructor
     //constructor() : this() {}

    fun loadImage(view: ImageView, image: String) {
        Glide.with(view.context).load(image).into(view)
    }

    override fun toString(): String {
        return "$name@$uid ($shipOwner)"
    }
}
