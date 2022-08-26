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

    fun getStringSharePreferences(context: Context, key: String): String {
        sharedPreferences =
            context.getSharedPreferences("UserInfo&Preferences", Context.MODE_PRIVATE)

        return sharedPreferences.getString(key, "")!!
    }

    fun setStringSharePreferences(context: Context, key: String, value: String) {
        sharedPreferences =
            context.getSharedPreferences("UserInfo&Preferences", Context.MODE_PRIVATE)
        sharedPreferencesEdit = sharedPreferences.edit()
        sharedPreferencesEdit.putString(key, value).apply()
    }

    fun getUID(): String? {
        return firebaseAuth.uid
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
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}