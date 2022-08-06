package com.example.boatbooking_1.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

// Singleton
object Util {

    val path: String = Environment.getExternalStorageDirectory().path
    
    val fStorage = FirebaseStorage.getInstance()
    val mDatabase = FirebaseDatabase.getInstance().reference
    private val _firebaseAuth = FirebaseAuth.getInstance()
    val firebaseAuth: FirebaseAuth
        get() = _firebaseAuth
    private val _fDatabase = FirebaseFirestore.getInstance()
    val fDatabase: FirebaseFirestore
        get() = _fDatabase

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEdit: SharedPreferences.Editor

    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    init {

    }

    fun getStringSharePreferences(context: Context, key: String) : String {
        sharedPreferences = context.getSharedPreferences("UserInfo&Preferences", Context.MODE_PRIVATE)

        return sharedPreferences.getString(key, "")!!
    }

    fun setStringSharePreferences(context: Context, key: String, value: String) {
        sharedPreferences = context.getSharedPreferences("UserInfo&Preferences", Context.MODE_PRIVATE)
        sharedPreferencesEdit = sharedPreferences.edit()
        sharedPreferencesEdit.putString(key, value).apply()
    }

    fun getUID(): String? {
        return firebaseAuth.uid
    }

    fun currentData(): String? {
        val calendar = Calendar.getInstance()
        return sdf().format(calendar.timeInMillis)
    }

    fun sdf(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.ITALY)
    }

    fun sdfBooking(): SimpleDateFormat {
        return SimpleDateFormat("dd-MM-yyyy", Locale.ITALY)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getTimeAgo(time: Long): String? {
        var time = time
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else {
            (diff / DAY_MILLIS).toString() + " days ago"
        }
    }

    fun updateOnlineStatus(status: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(
            getUID()!!
        )
        val map: MutableMap<String, Any> = HashMap()
        map["online"] = status
        databaseReference.updateChildren(map)
    }


}