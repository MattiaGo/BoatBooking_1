package com.example.boatbooking_1.repository

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.res.booleanResource
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.model.User
import com.google.firebase.database.DatabaseReference
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.boatbooking_1.model.ChatPreview
import com.example.boatbooking_1.model.MyMessage
import com.google.firebase.FirebaseError
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import kotlinx.coroutines.awaitAll
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

/*
    fun getStatus(): Boolean {
        var status: Boolean = false
        val mDatabase = FirebaseDatabase.getInstance().getReference("users")
        mDatabase.child(util.getUID()!!)
            .child("shipOwner").get().addOnSuccessListener { result ->
                status = result.value.toString().toBoolean()

            }
            .addOnFailureListener { exception ->
                Log.d("error", "Error getting documents: ", exception)
            }
        return status
    }

 */
    fun getStatus(): Boolean{
        val mDatabase = FirebaseDatabase.getInstance().reference
        var status: Boolean = false
        mDatabase.child("users")
            .child( Util.getUID()!!)
            .child("shipOwner")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    status = snapshot.value.toString().toBoolean()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("status", "onComplete: " + error)
                }

            })
        return status
    }

    fun editStatus(status: Boolean) {
        val userModel = liveData!!.value
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(Util.getUID()!!)
        val map: MutableMap<String, Any> = HashMap()
        map["shipOwner"] = status
        databaseReference!!.updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userModel!!.shipOwner = status
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