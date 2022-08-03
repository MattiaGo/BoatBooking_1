package com.example.boatbooking_1.repository

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.boatbooking_1.model.User
import com.example.boatbooking_1.ui.auth.LoginFragment.Companion.TAG
import com.example.boatbooking_1.utils.Util
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class UserProfileRepository {
    private var liveData: MutableLiveData<User?>? = null

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

    fun editEmail(email: String, context: Context) {
        val user = liveData!!.value
        val map: MutableMap<String, Any> = HashMap()
        map["email"] = email
        val fUser = Util.firebaseAuth.currentUser

        if (email != fUser!!.email) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Aggiornamento informazioni...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            Util.mDatabase.child("users").child(Util.getUID()!!).updateChildren(map)
                .addOnSuccessListener {
                    Log.d("editEmail", "onSuccess: Email updated")
//                Util.firebaseAuth.currentUser!!.updateEmail(email)
                    val credential = EmailAuthProvider.getCredential(
                        fUser.email.toString(),
                        Util.getStringSharePreferences(context, "password")
                    )
                    // Prompt the user to re-provide their sign-in credentials
                    // Prompt the user to re-provide their sign-in credentials
                    fUser.reauthenticate(credential)
                        .addOnCompleteListener {
                            Log.d("Firebase", "User re-authenticated.")
                            //Now change your email address \\
                            //----------------Code for Changing Email Address----------\\
                            fUser.updateEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        user!!.email = email
                                        liveData!!.value = user
                                        Log.d("Firebase", "User email address updated.")
                                        if (progressDialog.isShowing) {
                                            progressDialog.dismiss()
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    if (progressDialog.isShowing) {
                                        progressDialog.dismiss()
                                    }
                                }
                            //----------------------------------------------------------\\
                        }
                }
                .addOnFailureListener {
                    Log.d("editEmail", "onFailure: $it")
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
        }
    }

    private fun updateChatPreview() {
        val map: MutableMap<String, Any> = HashMap()
        map["user"] = liveData!!.value!!
        val uid = Util.firebaseAuth.currentUser!!.uid
        val keyList = mutableListOf<String>()
//        Log.d("key", Util.mDatabase.child("chats").child(Util.getUID()!!).key.toString())

        val query = Util.mDatabase.child("chats").get()

        query.addOnSuccessListener { dataSnapshot ->
//            Log.d("keyList", dataSnapshot.hasChildren().toString())

            for (snapshot in dataSnapshot.children) {
//                Log.d("keyList", snapshot.key.toString())
                keyList.add(snapshot.key.toString())
            }

            for (key in keyList) {
                if (key != uid) {
                    Util.mDatabase.child("chats/${key}/${uid}").updateChildren(map)
                        .addOnSuccessListener {
                            Log.d("updateChatPreview", "onSuccess: ChatPreview updated")
                        }
                }
            }
        }

//        Util.mDatabase.child("chats").child(Util.getUID()!!).child()
//            .addOnSuccessListener {
//                Log.d("updateChatPreview", "onSuccess: ChatPreview updated")
//            }
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

        // Fix virtual document on Firestore
        Util.fDatabase.collection("BoatAnnouncement")
            .document(Util.getUID()!!)
            .set(
                hashMapOf(
                    "id" to Util.getUID()
                )
            )
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
                    updateChatPreview()
                } else Log.d("name", "onComplete:" + task.exception)
            }
    }

    companion object {
        private var userProfileRepository: UserProfileRepository? = null
        val instance: UserProfileRepository
            get() = UserProfileRepository().also { userProfileRepository = it }
    }
}