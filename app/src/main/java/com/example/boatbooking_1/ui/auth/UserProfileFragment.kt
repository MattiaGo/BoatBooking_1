package com.example.boatbooking_1.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_EMAIL = "email"
private const val ARG_NAME = "name"

class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var fAuth: FirebaseAuth
    private lateinit var btnLogout: Button
    private lateinit var etEmail: EditText
    private lateinit var etName: EditText

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        /*mAuthListener = AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                Toast.makeText(context, "already logged", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        etEmail = binding.etEmail
        etName = binding.etName

        etEmail.setText(arguments?.getString(ARG_EMAIL))
        etName.setText(arguments?.getString(ARG_NAME))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //firebaseAuth.addAuthStateListener(mAuthListener)

        val user = Firebase.auth.currentUser
        if(!user!!.isEmailVerified) {
            Toast.makeText(context, "ricordati di confermare la mail", Toast.LENGTH_SHORT).show()

        }

        binding.logoutBtn.setOnClickListener{
            signOut()
        }
    }


    private fun signOut() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.account)
        }
    }
}