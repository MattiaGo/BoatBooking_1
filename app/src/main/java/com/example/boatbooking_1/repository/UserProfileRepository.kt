package com.example.boatbooking_1.repository

import android.util.Log
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.model.User
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.boatbooking_1.interfaces.FirebaseCallBackInterface
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.HashMap


class UserProfileRepository {
    private var liveData: MutableLiveData<User?>? = null
    private lateinit var myCallBack: FirebaseCallBackInterface

    val user: LiveData<User?>
        get() {
//            if (liveData == null || liveData!!.value!!.uid != Util.getUID()) { // Change account
            liveData = MutableLiveData()
            Util.mDatabase.child("users").child(Util.getUID()!!)
                .addValueEventListener(object : ValueEventListener {
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
//            }

            return liveData!!
        }

    fun editImage(uri: String) {
        val userModel = liveData!!.value
        val map: MutableMap<String, Any> = HashMap()
        map["image"] = uri

        Util.mDatabase.child("users").child(Util.getUID()!!).updateChildren(map)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userModel!!.image = uri
                    liveData!!.value = userModel
                    Log.d("image", "onComplete: Image updated")
                } else Log.d("image", "onComplete: " + task.exception)
            }
    }

//    fun getStatus(firebaseCallBack: FirebaseCallBackInterface) {
//        Util.mDatabase.child("users").child(Util.getUID()!!)
//            .child("shipOwner").get().addOnSuccessListener { result ->
//                firebaseCallBack.onCallbackForStatus(result.value.toString().toBoolean())
//            }
//            .addOnFailureListener { exception ->
//                Log.d("error", "Error getting documents: ", exception)
//            }
//    }

    fun isShipOwner(): Boolean {
        var isShipOwner = false

        Util.mDatabase.child("users").child(Util.getUID()!!).child("shipOwner").get()
            .addOnSuccessListener {
                isShipOwner = it.value as Boolean
//                Log.d("isShipOwner", it.value.toString())
            }
            .addOnFailureListener {
                Log.d("Firebase", it.toString())
            }

        return isShipOwner
    }

    fun editStatus(status: Boolean) {
        val userModel = liveData!!.value
        val map: MutableMap<String, Any> = HashMap()
        map["shipOwner"] = status

        Util.mDatabase.child("users").child(Util.getUID()!!).updateChildren(map)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userModel!!.shipOwner = status
                    liveData!!.value = userModel
                    Log.d("status", "onComplete: Status Updated")
                } else Log.d("status", "onComplete: " + task.exception)
            }
    }

    fun editName(name: String) {
        val userModel = liveData!!.value
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = name

        Util.mDatabase.child("users").child(Util.getUID()!!).updateChildren(map)
            .addOnCompleteListener { task ->
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