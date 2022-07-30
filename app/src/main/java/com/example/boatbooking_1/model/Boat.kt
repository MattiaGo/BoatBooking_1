package com.example.boatbooking_1.model

import android.os.Parcel
import android.os.Parcelable

class Boat(
    private var _builder: String? = null,
    private var _model: String? = null,
    private var _year: Int? = null,
    private var _length: Int? = null,
    private var _passengers: Int? = null,
    private var _license: Boolean? = null,
) : Parcelable {

    val builder: String get() = _builder.toString()
    val model: String get() = _model.toString()
    val year: Int get() = _year.toString().toInt()
    val length: Int get() = _length.toString().toInt()
    val passengers: Int get() = _passengers.toString().toInt()
    val license: Boolean get() = _license.toString().toBooleanStrictOrNull() == true

    constructor(parcel: Parcel) : this() {
        _builder = parcel.readString()
        _model = parcel.readString()
        _year = parcel.readValue(Int::class.java.classLoader) as? Int
        _length = parcel.readValue(Int::class.java.classLoader) as? Int
        _passengers = parcel.readValue(Int::class.java.classLoader) as? Int
        _license = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
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
        parcel.writeString(_builder)
        parcel.writeString(_model)
        parcel.writeValue(_year)
        parcel.writeValue(_length)
        parcel.writeValue(_passengers)
        parcel.writeValue(_license)
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
        return "${_builder.toString()}\n" +
                "${_model.toString()}\n" +
                "${_year.toString()}\n" +
                "${_length.toString()}\n" +
                "${_passengers.toString()}\n" +
                "${_license.toString()}"

    }
}