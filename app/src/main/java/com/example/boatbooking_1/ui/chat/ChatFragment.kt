package com.example.boatbooking_1.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentChatBinding
import com.example.boatbooking_1.model.MyMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_UID = "uid"

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageArrayList: ArrayList<MyMessage>
    private lateinit var sendButton: ImageView
    private lateinit var messageBox: TextView
    private lateinit var chatTitle: TextView
    private lateinit var mDatabase: DatabaseReference
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private lateinit var myMessageAdapter: MyMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser

        senderRoom = user!!.uid
        Log.d("UID", senderRoom.toString())
        receiverRoom = arguments?.getString(ARG_UID).toString()
        Log.d("UID", receiverRoom.toString())

        messageArrayList = ArrayList()
        myMessageAdapter = MyMessageAdapter(messageArrayList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false)
        binding = FragmentChatBinding.inflate(inflater, container, false)
        chatTitle = binding.tvChatTitle
        sendButton = binding.btnSend
        messageBox = binding.etTextMessage
        chatRecyclerView = binding.rvChatMessages

        chatTitle.text = arguments?.getString("name")

        chatRecyclerView.layoutManager = LinearLayoutManager(this.context)
        chatRecyclerView.adapter = myMessageAdapter
//        chatRecyclerView.setHasFixedSize(true)

        // View chat messages
        mDatabase.child("messages").child(senderRoom).child(receiverRoom)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageArrayList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(MyMessage::class.java)
                        messageArrayList.add(message!!)
                    }

                    myMessageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.btnBack.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToMainMessages()
            findNavController().navigate(action)
        }

        // Update chat messages on Firebase
        sendButton.setOnClickListener {
            if (messageBox.text.isNotEmpty()) {
                val message = messageBox.text.toString()
                val messageObject = MyMessage(message, FirebaseAuth.getInstance().currentUser!!.uid)

                mDatabase.child("messages").child(senderRoom).child(receiverRoom).push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDatabase.child("messages").child(receiverRoom).child(senderRoom).push()
                            .setValue(messageObject).addOnSuccessListener {
                                // Update chat preview last message [chats > uid > uid]
                                mDatabase.child("chats").child(senderRoom).child(receiverRoom)
                                    .child("lastMessage").setValue(messageObject.message)
                                mDatabase.child("chats").child(senderRoom).child(receiverRoom)
                                    .child("timestamp").setValue(messageObject.timestamp)

                                mDatabase.child("chats").child(receiverRoom).child(senderRoom)
                                    .child("lastMessage").setValue(messageObject.message)
                                mDatabase.child("chats").child(receiverRoom).child(senderRoom)
                                    .child("timestamp").setValue(messageObject.timestamp)
                            }
                    }

                messageBox.text = ""
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param argUID
         * @return A new instance of fragment Chat.
         **/

        @JvmStatic
        fun newInstance(argUID: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UID, argUID)
                }
            }
    }
}