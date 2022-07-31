package com.example.boatbooking_1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.interfaces.FirebaseCallBackInterface
import com.example.boatbooking_1.repository.UserProfileRepository
import com.example.boatbooking_1.model.User


class UserProfileViewModel : ViewModel() {

    private var userProfileRepository: UserProfileRepository = UserProfileRepository.instance

    fun getUser(): LiveData<User?> {
        return userProfileRepository.user
    }

    fun editImage(uri: String) {
        userProfileRepository.editImage(uri)
    }

    fun editStatus(status: Boolean) {
        userProfileRepository.editStatus(status)
    }

    fun editName(name: String) {
        userProfileRepository.editName(name)
    }

    fun isOwnerShip() : Boolean {
       return userProfileRepository.isShipOwner()
    }
}
