package com.example.boatbooking_1.model

import android.os.Parcel
import android.os.Parcelable

class Boat(
    var name: String? = null,
    var model: String? = null,
    var builder: String? = null,
    var year: Int? = null,
    var length: Int? = null,
    var passengers: Int? = null,
    var beds: Int? = null,
    var cabins: Int? = null,
    var bathrooms: Int? = null,
    //var license: Boolean? = null,
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        model = parcel.readString()
        builder = parcel.readString()
        year = parcel.readValue(Int::class.java.classLoader) as? Int
        length = parcel.readValue(Int::class.java.classLoader) as? Int
        passengers = parcel.readValue(Int::class.java.classLoader) as? Int
        beds = parcel.readValue(Int::class.java.classLoader) as? Int
        cabins = parcel.readValue(Int::class.java.classLoader) as? Int
        bathrooms = parcel.readValue(Int::class.java.classLoader) as? Int
        //license = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }


//    constructor() {}

//    constructor(
//        builder: String?,
//        model: String?,
//        year: Int?,
//        length: Int?,
//        passengers: Int?,
//        license: Boolean?
//    ) {
//        this.builder = builder
//        this.model = model
//        this.year = year
//        this.length = length
//        this.passengers = passengers
//        this.license = license
//    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(model)
        parcel.writeString(builder)
        parcel.writeValue(year)
        parcel.writeValue(length)
        parcel.writeValue(passengers)
        parcel.writeValue(beds)
        parcel.writeValue(cabins)
        parcel.writeValue(bathrooms)
        //parcel.writeValue(license)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Boat> {
        override fun createFromParcel(parcel: Parcel): Boat {
            return Boat(parcel)
        }

        override fun newArray(size: Int): Array<Boat?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "${name.toString()}\n" +
                "${model.toString()}\n" +
                "${builder.toString()}\n" +
                "${year.toString()}\n" +
                "${length.toString()}\n" +
                "${beds.toString()}\n" +
                "${cabins.toString()}\n" +
                "${bathrooms.toString()}\n"
        //"${license.toString()}\n"
    }
}