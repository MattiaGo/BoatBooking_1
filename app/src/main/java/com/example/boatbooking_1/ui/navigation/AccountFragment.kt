package com.example.boatbooking_1.ui.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.boatbooking_1.LoginViewModel
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentAccountBinding
import com.example.boatbooking_1.databinding.FragmentUserProfileBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AccountFragment : Fragment() {

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: FragmentAccountBinding
    private lateinit var userProfileBinding: FragmentUserProfileBinding


    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var fAuth: FirebaseAuth

    // for google sign in
    private val WEB_CLIENT_ID =
        "949642030597-hg459s2m916vci5gnppbrrec89q3vdg9.apps.googleusercontent.com"
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 0

    private lateinit var mFirebaseAuth: FirebaseAuth
    private val mAuthStateListener: AuthStateListener? = null

    private var mGoogleApiClient // for google sign in
            : GoogleApiClient? = null

    private lateinit var googleSignInClient: GoogleSignInClient
    companion object {
        const val GOOGLE_SIGN_IN = 1903
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFBGoogleSignIn()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //observeAuthenticationState()
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        //email = binding.textEmailAddress
        //password = binding.textPassword
        //fAuth = Firebase.auth

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                Toast.makeText(context, "cazzo", Toast.LENGTH_SHORT).show()
                //mGoogleSignInTextView.setText("failed")
            }
        //} else if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE) {
        //    mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        //} else if (requestCode == TWITTER_LOG_IN_REQUEST_CODE) {
        //    mTwitterLoginButton.onActivityResult(requestCode, resultCode, data)
        //}
    }
    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.stopAutoManage(requireActivity())
            mGoogleApiClient!!.disconnect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.stopAutoManage(requireActivity())
            mGoogleApiClient!!.disconnect()
        }
    }

    // [START auth_with_google]
    private fun initFBGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()
        val context: Context? = context
        mGoogleApiClient = context?.let {
            GoogleApiClient.Builder(it)
                .enableAutoManage(
                    requireActivity()
                //occhio! textEmailAddress
                ) { connectionResult -> binding.textEmailAddress.setText(connectionResult.errorMessage) }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        }
    }

    private fun signInWithGoogleSignIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    val action = AccountFragmentDirections.actionMainAccountToUserProfile(email = "cazzo")
                    findNavController().navigate(action)
                } else {
                    binding.loginBtn.isEnabled = true
                    binding.loginBtn.alpha = 1.0f
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
    // [END auth_with_google]


    private fun getGSO(): GoogleSignInOptions {
        return  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }



    private fun firebaseSignIn() {
        binding.loginBtn.isEnabled = false
        binding.loginBtn.alpha = 0.5f

        fAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val action = AccountFragmentDirections.actionMainAccountToUserProfile(email = "cazzo")
                    findNavController().navigate(action)
                } else {
                    binding.loginBtn.isEnabled = true
                    binding.loginBtn.alpha = 1.0f
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observeAuthenticationState()

        email = binding.textEmailAddress
        password = binding.textPassword
        fAuth = Firebase.auth

        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGSO())

        binding.googleBtn.setOnClickListener{
            signInWithGoogleSignIn()
        }

        binding.loginBtn.setOnClickListener {
            firebaseSignIn()
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


    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: (1) show a logout button and (2) display their name.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {
        //val factToDisplay = viewModel.getFactToDisplay(requireContext())

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {

                    //findNavController().navigate(R.id.ic_account)
                    //binding.loginBtn.text = getString(R.string.logout_button_text)
                    //binding.loginBtn.setOnClickListener {
                    //    AuthUI.getInstance().signOut(requireContext())
                    //}
                }
                else -> {
                    //binding.welcomeText.text = factToDisplay

                    //binding.loginBtn.text = getString(R.string.login_button_text)
                    // binding.loginBtn.setOnClickListener {
                    //    findNavController().navigate(R.id.userProfile)
                    //}
                }
            }
        })
    }

    private fun getFactWithPersonalization(fact: String): String {
        return String.format(
            resources.getString(
                R.string.welcome_message_authed,
                FirebaseAuth.getInstance().currentUser?.displayName,
                Character.toLowerCase(fact[0]) + fact.substring(1)
            )
        )
    }
}