package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.databinding.FragmentMessagesBinding
import com.example.boatbooking_1.model.ChatPreview
import com.example.boatbooking_1.model.ChatPreviewAdapter
import com.example.boatbooking_1.model.MyMessage
import com.example.boatbooking_1.model.User
import com.example.boatbooking_1.ui.auth.UserProfileFragmentDirections
import com.google.common.collect.Lists.reverse
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


/**
 * A simple [Fragment] subclass.
 * Use the [Messages.newInstance] factory method to
 * create an instance of this fragment.
 */

class MessagesFragment : Fragment() {
    // View Binding
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var mDatabase: DatabaseReference
    private lateinit var fAuth: FirebaseAuth

    private lateinit var chatPreviewRecyclerView: RecyclerView
    private lateinit var chatPreviewAdapter: ChatPreviewAdapter
    private lateinit var chatPreviewList: ArrayList<ChatPreview>

    lateinit var userList: ArrayList<User>
    lateinit var lastMessageList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatPreviewList = ArrayList()
        mDatabase = FirebaseDatabase.getInstance().reference
        chatPreviewAdapter = ChatPreviewAdapter(chatPreviewList)



//        userList = arrayOf(
//            User("Admin", "admin@boatbooking.com", "0"),
//            User("Matteo", "matteo@mail.com", "1"),
//            User("Mattia", "mattia@mail.com", "2")
//        )
//
//        lastMessageList = arrayOf(
//            "Ciao, come stai?",
//            "È possibile effettuare una prenotazione?",
//            "Mi dispiace non è più disponibile!"
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_messages, container, false)

        binding = FragmentMessagesBinding.inflate(inflater, container, false)

        chatPreviewRecyclerView = binding.rvChatPreview
        chatPreviewRecyclerView.layoutManager = LinearLayoutManager(this.context)
        chatPreviewRecyclerView.setHasFixedSize(true)
        chatPreviewRecyclerView.adapter = chatPreviewAdapter
        fAuth = FirebaseAuth.getInstance()

        val currentUser = fAuth.currentUser

        if (currentUser != null) {                  //questa
            binding.rvChatPreview.isVisible = true
            mDatabase.child("chats").child(currentUser!!.uid).orderByChild("timestamp")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        chatPreviewList.clear()

                        for (postSnapshot in snapshot.children) {
//                        val uid = postSnapshot.key.toString()
//                        mDatabase.child("users").child(uid).get().addOnSuccessListener {
//                            val user = it.getValue(User::class.java)
//                            name = user?.name
//                            Log.i("firebase", "Got value ${it.value}")
//                        }.addOnFailureListener {
//                            Log.e("firebase", "Error getting data", it)
//                        }

                            val chatPreview = postSnapshot.getValue(ChatPreview::class.java)

                            chatPreviewList.add(chatPreview!!)
                        }

                        chatPreviewList.reverse()

                        chatPreviewAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                    }

                })

        } else{
            binding.msgLogin.isVisible = true
        }
        return binding.root
    }
}