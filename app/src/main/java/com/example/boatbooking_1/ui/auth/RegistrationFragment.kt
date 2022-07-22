package com.example.boatbooking_1.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentRegistrationBinding
import com.example.boatbooking_1.ui.navigation.AccountFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var cnfPassword: EditText
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
        cnfPassword = binding.textPasswordConfirm
        fAuth = Firebase.auth

        return binding.root
    }


    private fun firebaseSignUp() {
        binding.registrationBtn.isEnabled = false
        binding.registrationBtn.alpha = 0.5f

        fAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action = RegistrationFragmentDirections.actionRegistrationToMainAccount()
                    findNavController().navigate(action)
                } else {
                    binding.registrationBtn.isEnabled = true
                    binding.registrationBtn.alpha = 1f
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationToMainAccount()
            findNavController().navigate(action)
        }

        binding.registrationBtn.setOnClickListener {
            firebaseSignUp()
        }
    }
}