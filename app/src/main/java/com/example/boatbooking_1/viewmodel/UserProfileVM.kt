package com.example.boatbooking_1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.repository.ProfileRepository
import com.example.boatbooking_1.model.User


class UserProfileVM : ViewModel() {

    var profileRepository: ProfileRepository = ProfileRepository.instance

    fun getUser(): LiveData<User?>? {
        return profileRepository.user
    }

    fun editImage(uri: String) {
        profileRepository.editImage(uri)
    }

    fun editStatus(status: Boolean) {
        profileRepository.editStatus(status)
    }

    fun edtUsername(name: String) {
        profileRepository.editUsername(name)
    }
}
