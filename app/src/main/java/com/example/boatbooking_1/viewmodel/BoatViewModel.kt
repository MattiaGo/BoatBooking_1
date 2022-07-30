package com.example.boatbooking_1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.model.Boat

class BoatViewModel() : ViewModel() {
    private var _boat: Boat? = null
    val boat: Boat?
        get() = _boat

    private var _yearPosition: Int = 0
    val yearPosition: Int
        get() = _yearPosition
    private var _lengthPosition: Int = 0
    val lengthPosition: Int
        get() = _lengthPosition

    fun setBoat(boat: Boat?, yearPos: Int, lengthPos: Int) {
        _boat = boat
        saveYearSpinnerPosition(yearPos)
        saveLengthSpinnerPosition(lengthPos)
    }

    private fun saveYearSpinnerPosition(pos: Int) {
        _yearPosition = pos
    }

    private fun saveLengthSpinnerPosition(pos: Int) {
        _lengthPosition = pos
    }
}