package com.example.boatbooking_1.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentPasswordResetBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordResetFragment : Fragment() {

    private lateinit var binding: FragmentPasswordResetBinding
    private lateinit var email: EditText
    private lateinit var message: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPasswordResetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = binding.textEmailAddress
        message = binding.message

        binding.backBtn.setOnClickListener {
            val action = PasswordResetFragmentDirections.actionPasswordResetFragmentToMainAccount()
            findNavController().navigate(action)
        }
        binding.sendResetPwdBtn.setOnClickListener {
            sendResetPassword()
        }
    }

    private fun sendResetPassword(){
        if (!email.text.isEmpty()) {
            Firebase.auth.sendPasswordResetEmail(email.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        message.text = "Mail per il reset password inviata"
                        message.setTextColor(getResources().getColor(R.color.black))
                        message.isVisible = true
                    }
                }
        } else {
            message.text = "Inserire la mail con la quale si è registrati"
            message.isVisible = true
        }
    }
}