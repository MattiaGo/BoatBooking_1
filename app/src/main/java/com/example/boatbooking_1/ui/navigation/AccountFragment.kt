package com.example.boatbooking_1.ui.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentAccountBinding
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AccountFragment : Fragment() {
    // Get a reference to the ViewModel scoped to this Fragment
    private lateinit var binding: FragmentAccountBinding

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginMessageError: TextView

    //for google log in
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()


    companion object {
        const val GOOGLE_SIGN_IN = 1903
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAuth = Firebase.auth

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            findNavController().graph.setStartDestination(R.id.userProfile)
        }
//        if (user != null) {
//            updateUI(user)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = binding.textEmailAddress
        password = binding.textPassword

        loginMessageError = binding.loginErrorMessage

        binding.googleBtn.setOnClickListener {
            signInWithGoogle()
        }

        binding.loginBtn.setOnClickListener {
            signInWithEmailAndPassword()
        }

        binding.pwdForget.setOnClickListener {
            val action = AccountFragmentDirections.actionMainAccountToPasswordResetFragment()
            findNavController().navigate(action)
        }

        binding.signUpBtn.setOnClickListener {
            val action = AccountFragmentDirections.actionMainAccountToRegistration()
            findNavController().navigate(action)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)   //account.idToken!!
            } catch (e: ApiException) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()//handle error
            }
        }
    }

    private fun updateUI() {
        userProfileViewModel.activateFavoritesIfNot()
        userProfileViewModel.activateLastSeenIfNot()
        val action = AccountFragmentDirections.actionMainAccountToUserProfile()
        findNavController().navigate(action)
    }

    // [EMAIL/PWD Authentication]
    private fun signInWithEmailAndPassword() {
        if (email.text.isEmpty() or password.text.isEmpty()) {
            loginMessageError.text = "Inserire i dati per il login"
            loginMessageError.isVisible = true
        } else {
            mFirebaseAuth.signInWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Util.setStringSharePreferences(requireContext(), "password", password.text.toString())
                        updateUI()
                    } else {
                        loginMessageError.text = task.exception?.message
                        loginMessageError.isVisible = true
                        //Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    userProfileViewModel.addUserToDatabase(
                        acct.givenName!!,
                        acct.email!!,
                        Util.getUID()!!
                    )
                    updateUI()
                } else {
                    binding.loginBtn.isEnabled = true
                    binding.loginBtn.alpha = 1.0f
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGSO())
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun getGSO(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    // [END auth_with_google]
}