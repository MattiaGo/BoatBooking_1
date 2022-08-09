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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentRegistrationBinding
import com.example.boatbooking_1.model.User
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    private lateinit var signUpErrorMessage: TextView
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        signUpErrorMessage = binding.signUpErrorMessage

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
                        (et1.isNotEmpty() && et2.isNotEmpty() && et3.isNotEmpty() && et4.isNotEmpty())
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

        if (email.text.isEmpty() or password.text.isEmpty() or confirmPassword.text.isEmpty()) {
            signUpErrorMessage.text = "Attenzione compilare tutti i campi"
            signUpErrorMessage.isVisible = true
        } else {
            Util.firebaseAuth.createUserWithEmailAndPassword(
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
                                        "Una mail di conferma è stata inviata all'indirizzo e-mail utilizzato durante la registrazione"
                                    signUpErrorMessage.setTextColor(resources.getColor(R.color.black))
                                    signUpErrorMessage.isVisible = true

                                    userProfileViewModel.addUserToDatabase(
                                        name.text.toString(),
                                        user.email.toString(),
                                        user.uid
                                    )
                                }
                                binding.registrationBtn.isVisible = false
                            }

                        Util.firebaseAuth.signOut()

                    } else {
                        signUpErrorMessage.text = task.exception?.message
                        signUpErrorMessage.isVisible = true
                    }
                }
            binding.registrationBtn.isEnabled = true
            binding.registrationBtn.alpha = 1f
        }
    }

}