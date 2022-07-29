package com.example.boatbooking_1.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boatbooking_1.Repository.ProfileRepository
import com.example.boatbooking_1.model.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


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
