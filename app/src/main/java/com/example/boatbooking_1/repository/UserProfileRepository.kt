package com.example.boatbooking_1.repository

import android.util.Log
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.model.User
import com.google.firebase.database.DatabaseReference
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.HashMap

class UserProfileRepository {
    private var databaseReference: DatabaseReference? = null
    private var liveData: MutableLiveData<User?>? = null
    val user: LiveData<User?>
        get() {
            if (liveData == null) {
                liveData = MutableLiveData()
                databaseReference = FirebaseDatabase.getInstance().getReference("users")
                databaseReference!!.child(Util.getUID()!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val userModel = dataSnapshot.getValue(
                                    User::class.java
                                )
                                liveData!!.value = userModel
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
            }

            return liveData!!
        }

    fun editImage(uri: String) {
        val userModel = liveData!!.value
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(Util.getUID()!!)
        val map: MutableMap<String, Any> = HashMap()
        map["image"] = uri
        databaseReference!!.updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userModel!!.image = uri
                liveData!!.value = userModel
                Log.d("image", "onComplete: Image updated")
            } else Log.d("image", "onComplete: " + task.exception)
        }
    }

    fun editStatus(status: Boolean) {
        val userModel = liveData!!.value
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(Util.getUID()!!)
        val map: MutableMap<String, Any> = HashMap()
        map["status"] = status
        databaseReference!!.updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userModel!!.isShipOwner = status
                liveData!!.value = userModel
                Log.d("status", "onComplete: Status Updated")
            } else Log.d("status", "onComplete: " + task.exception)
        }
    }

    fun editUsername(name: String) {
        val userModel = liveData!!.value
        databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(Util.getUID()!!)
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = name
        databaseReference!!.updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userModel!!.name = name
                liveData!!.value = userModel
                Log.d("name", "onComplete: Name Updated")
            } else Log.d("name", "onComplete:" + task.exception)
        }
    }

    companion object {
        private var userProfileRepository: UserProfileRepository? = null
        val instance: UserProfileRepository
            get() = UserProfileRepository().also { userProfileRepository = it }
    }
}