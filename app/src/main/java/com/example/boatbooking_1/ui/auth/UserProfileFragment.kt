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
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.example.boatbooking_1.model.ChatPreview
import com.example.boatbooking_1.model.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_EMAIL = "email"
private const val ARG_NAME = "name"

class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var btnAddBoat: Button
    private lateinit var etEmail: TextInputEditText
    private lateinit var etName: TextInputEditText
    private lateinit var tvLocation: MaterialTextView
    private lateinit var mDatabase: DatabaseReference

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        disableOnBackClick()

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
        tvLocation = binding.tvLocation

        btnAddBoat = binding.btnAddBoat

//        if (firebaseAuth.currentUser != null) {
//            etEmail.setText(firebaseAuth.currentUser?.email)
//            etName.setText(firebaseAuth.currentUser?.displayName)
//        } else {
//            etEmail.setText(arguments?.getString(ARG_EMAIL))
//            etName.setText(arguments?.getString(ARG_NAME))
//        }

        mDatabase.child("users").child(firebaseAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)

                    etName.setText(user?.name.toString())
                    etEmail.setText(user?.email.toString())
                    tvLocation.text = user?.location.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                }

            })

        return binding.root
    }

    private fun disableOnBackClick() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // With blank your fragment BackPressed will be disabled.
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //firebaseAuth.addAuthStateListener(mAuthListener)

        val user = Firebase.auth.currentUser

        binding.logoutBtn.setOnClickListener{
            signOut()
        }

        binding.btnAddBoat.setOnClickListener {
            val action = UserProfileFragmentDirections.actionUserProfileToAddBoatFragment()
            findNavController().navigate(action)
        }
    }


    private fun signOut() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            firebaseAuth.signOut()
            val action = UserProfileFragmentDirections.actionUserProfileToMainAccount()
            findNavController().navigate(action)
        }

        btnAddBoat.setOnClickListener {
            val action = UserProfileFragmentDirections.actionUserProfileToAddBoatFragment()
            findNavController().navigate(action)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(email: String?, name: String?) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMAIL, email)
                    putString(ARG_NAME, name)
                }
            }
    }
}