package com.example.boatbooking_1.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.viewModels.UserProfileVM
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentRegistrationBinding
import com.example.boatbooking_1.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

private lateinit var database: DatabaseReference



class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var cnfPassword: EditText
    private lateinit var signUpErrorMessage: TextView

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    //internal lateinit var viewModel: UserProfileVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseAuth = Firebase.auth
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        name = binding.textName
        email = binding.textEmailAddress
        password = binding.textPassword
        confirmPassword = binding.textPasswordConfirm

        cnfPassword = binding.textPasswordConfirm
        signUpErrorMessage = binding.signUpErrorMessage
        mFirebaseAuth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationBtn.isEnabled = false
        binding.registrationBtn.alpha = 0.8f

        val editTexts = listOf(name, email, password, confirmPassword)
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val et1 = name.text.toString().trim()
                    val et2 = email.text.toString().trim()
                    val et3 = password.text.toString().trim()
                    val et4 = confirmPassword.toString().trim()

                    binding.registrationBtn.isEnabled =
                        et1.isNotEmpty() && et2.isNotEmpty() && et3.isNotEmpty() && et4.isNotEmpty()
                    if (binding.registrationBtn.isEnabled) binding.registrationBtn.alpha = 1.0f
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun afterTextChanged(
                    s: Editable
                ) {
                }
            })
        }

        binding.backBtn.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationToMainAccount()
            findNavController().navigate(action)
        }

        binding.registrationBtn.setOnClickListener {
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        binding.registrationBtn.isEnabled = false
        binding.registrationBtn.alpha = 0.5f

        if (email.text.isEmpty() or password.text.isEmpty() or cnfPassword.text.isEmpty()) {
            signUpErrorMessage.text = "Attenzione compilare tutti i campi"
            signUpErrorMessage.isVisible = true
        } else {
            mFirebaseAuth.createUserWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = Firebase.auth.currentUser
                        user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    signUpErrorMessage.text =
                                        "Una mail di conferma è stata inviata all'indirizzo"
                                    signUpErrorMessage.setTextColor(getResources().getColor(R.color.black))
                                    signUpErrorMessage.isVisible = true

                                    addUserToDatabase(
                                        name.text.toString(),
                                        user.email.toString(),
                                        user.uid
                                    )
                                }
                                binding.registrationBtn.isVisible = false
                            }
                        if (user != null) {
                            mFirebaseAuth = FirebaseAuth.getInstance()
                            mFirebaseAuth.signOut()
                        }
                    } else {
                        signUpErrorMessage.text = task.exception?.message
                        signUpErrorMessage.isVisible = true
                    }
                }
            binding.registrationBtn.isEnabled = true
            binding.registrationBtn.alpha = 1f
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(uid).setValue(User(name, email))

        // Test
        //storeFakeDataOnDatabase(uid)
    }

    /*
    private fun storeFakeDataOnDatabase(uid: String) {
        mDatabase.child("chats").child(uid).child("VED1f7yQbUc7YWrvKglJrNMfu8u1")
            .setValue(
                ChatPreview(
                    User(
                        "Test 1",
                        "e-mail",
                        "VED1f7yQbUc7YWrvKglJrNMfu8u1",
                        "Brescia",
                        false
                    ), "Ciao!", Timestamp.now().seconds
                )
            )
        mDatabase.child("chats").child(uid).child("kXcSNSsofwWCng7e0kGtKAJclRb2")
            .setValue(
                ChatPreview(
                    User(
                        "Test 2",
                        "e-mail",
                        "kXcSNSsofwWCng7e0kGtKAJclRb2",
                        "Brescia",
                        false
                    ), "Hola...", Timestamp.now().seconds
                )
            )
    }*/


//    private fun firebaseSignUp() {
//        mFirebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val profileUpdates = UserProfileChangeRequest.Builder()
//                        .setDisplayName(name.text.toString())
//                        .build()
//
//                    mFirebaseAuth.currentUser!!.updateProfile(profileUpdates)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Log.d("NAME_UPDATED", "User profile updated.")
//                            }
//                        }
//                    val action = RegistrationFragmentDirections.actionRegistrationToUserProfile(email.text.toString(), name.text.toString())
//                    findNavController().navigate(action)
//                } else {
//                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
}