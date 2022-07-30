package com.example.boatbooking_1.ui.auth

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.example.boatbooking_1.model.User
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_EMAIL = "email"
private const val ARG_NAME = "name"

class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var imageUri: Uri
    private lateinit var util: Util

    //private lateinit var permissions: Permissions
    private lateinit var alertDialog: AlertDialog
    private lateinit var user: User
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEdit: SharedPreferences.Editor


    private lateinit var btnAddBoat: Button
    private lateinit var etEmail: TextInputEditText
    private lateinit var etName: TextInputEditText
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

        sharedPreferences =
            context!!.getSharedPreferences("UserInfo&Preferences", Context.MODE_PRIVATE)
        sharedPreferencesEdit = sharedPreferences.edit()

        userProfileViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application).create(
                UserProfileViewModel::class.java
            )


        val model: UserProfileViewModel by activityViewModels()
        /*model.getUser()?.observe(viewLifecycleOwner,Observer<User?>{ user ->
            etName.setText(user.name)
        })

         */

        sharedPreferencesEdit.putString("name", "paperino").apply()

        val observer: Observer<User?> =
            Observer<User?> { userModel ->
                binding.user = userModel
                //user = userModel
                val name: String? = userModel.name
                val email: String? = userModel.email
                binding.etName.setText(name)
                binding.etEmail.setText(email)
            }

        userProfileViewModel.getUser().observe(viewLifecycleOwner, observer)
        
        btnAddBoat = binding.myBoatBtn

//        if (firebaseAuth.currentUser != null) {
//            etEmail.setText(firebaseAuth.currentUser?.email)
//            etName.setText(firebaseAuth.currentUser?.displayName)
//        } else {
//            etEmail.setText(arguments?.getString(ARG_EMAIL))
//            etName.setText(arguments?.getString(ARG_NAME))
//        }

        /*mDatabase.child("users").child(firebaseAuth.currentUser!!.uid)
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

         */

        /*binding.button2.setOnClickListener {
            //val name = binding.etName.text.toString()
            //profileViewModel.edtUsername(name)
            etName.setText(sharedPreferences.getString("name",null))


        }*/

        return binding.root
    }


    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
                val name = data.getStringExtra("name")
                profileViewModel.edtUsername(name)
                sharedPreferences.putString("username", name).apply()
            }
    }
*/

    private fun disableOnBackClick() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // With blank your fragment BackPressed will be disabled.
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //firebaseAuth.addAuthStateListener(mAuthListener)

        val user = Firebase.auth.currentUser

        binding.logoutBtn.setOnClickListener {
            signOut()
        }

        binding.myBoatBtn.setOnClickListener {
            val action = UserProfileFragmentDirections.actionUserProfileToAddBoatFragment()
            findNavController().navigate(R.id.account)
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