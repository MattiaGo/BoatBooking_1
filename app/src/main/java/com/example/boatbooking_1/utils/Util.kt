package com.example.boatbooking_1.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

// Singleton
object Util {

    val mDatabase = FirebaseDatabase.getInstance().reference
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS


    fun getUID(): String? {
        return firebaseAuth.uid
    }

    fun currentData(): String? {
        val calendar = Calendar.getInstance()
        return sdf().format(calendar.timeInMillis)
    }

    fun sdf(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd hh-mm-ss a", Locale.US)
    }

    fun hideKeyBoard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        assert(inputMethodManager != null)
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