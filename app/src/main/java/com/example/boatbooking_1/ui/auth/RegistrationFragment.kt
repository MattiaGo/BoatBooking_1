package com.example.boatbooking_1.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var fAuth: FirebaseAuth

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

        fAuth = Firebase.auth

        return binding.root
    }


    private fun firebaseSignUp() {
        fAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name.text.toString())
                        .build()

                    fAuth.currentUser!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("NAME_UPDATED", "User profile updated.")
                            }
                        }
                    val action = RegistrationFragmentDirections.actionRegistrationToUserProfile(
                        email.text.toString(),
                        name.text.toString()
                    )
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
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
}