package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.ChatPreview
import com.example.boatbooking_1.model.User
import com.example.boatbooking_1.utils.Util
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

//    private lateinit var binding: FragmentHomeBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)

        if (Firebase.auth.currentUser != null) {
        Toast.makeText(context, Firebase.auth.currentUser!!.uid, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, Firebase.auth.currentUser.toString(), Toast.LENGTH_SHORT).show()
        }

        // Test
        //storeFakeDataToDatabase()

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    /*
    private fun storeFakeDataToDatabase() {
        Util.mDatabase.child("chats").child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .child("LD3qWBHBFmWVN87S1r9reBgGx6H3")
            .setValue(
                ChatPreview(
                    user = User(
                        "Matteo",
                        "",
                        "LD3qWBHBFmWVN87S1r9reBgGx6H3",
                        "Brescia",
                        "",
                        false
                    ), "Ciao!", timestamp = Timestamp.now().seconds
                )
            )

        Util.mDatabase.child("chats").child("LD3qWBHBFmWVN87S1r9reBgGx6H3")
            .child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .setValue(
                ChatPreview(
                    user = User(
                        "Admin",
                        "",
                        "2PH1sbnjT4e1RkI0TYD6X1e1nD72",
                        "Brescia",
                        "",
                        false
                    ), "Ciao!", timestamp = Timestamp.now().seconds
                )
            )

        Util.mDatabase.child("users").child("2PH1sbnjT4e1RkI0TYD6X1e1nD72")
            .setValue(
                User(
                    "Admin",
                    "admin@ciao.com",
                    "2PH1sbnjT4e1RkI0TYD6X1e1nD72",
                    "Brescia",
                    "",
                    isShipOwner = true
                )
            )

        Util.mDatabase.child("users").child("LD3qWBHBFmWVN87S1r9reBgGx6H3")
            .setValue(
                User(
                    "Matteo",
                    "ciao@mail.org",
                    "LD3qWBHBFmWVN87S1r9reBgGx6H3",
                    "Brescia",
                    "",
                    false
                )
            )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

     */
}