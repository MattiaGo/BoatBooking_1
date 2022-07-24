package com.example.boatbooking_1.ui.chat

import android.os.Bundle
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
import com.example.boatbooking_1.model.MyMessageAdapter

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

    // Fake data to test RecyclerView
    lateinit var messageList: Array<MyMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageList = arrayOf(
            MyMessage("Ciao!", "0"),
            MyMessage("Ciao, come stai?", "1"),
            MyMessage("Bene dai, tu?", "0"),
            MyMessage("Bene", "1"),
            MyMessage("Ciao!", "1"),
            MyMessage("Okay", "0"),
            MyMessage("Arrivederci", "0"),
        )
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        arguments?.getString("ARG_UID")?.let {
//            title = it
//        }
//    }

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

        chatTitle.text = arguments?.getString("uid") ?: "null"

        chatRecyclerView.layoutManager = LinearLayoutManager(this.context)
        chatRecyclerView.setHasFixedSize(true)

        messageArrayList = arrayListOf()
        getFakeData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            val action = ChatFragmentDirections.actionChatFragmentToMainMessages()
            findNavController().navigate(action)
        }

//        sendButton.setOnClickListener {
//            if (messageBox.text.isNotEmpty()) {
//                val message = messageBox.text.toString()
//                val messageObject = MyMessage(message, "0")
//
//                messageArrayList.add(messageObject)
//                chatRecyclerView.adapter = MyMessageAdapter(messageArrayList)
//
//                messageBox.text = ""
//            }
//        }
    }

    private fun getFakeData() {
        for (message in messageList) {
            messageArrayList.add(message)
        }

        chatRecyclerView.adapter = MyMessageAdapter(messageArrayList)
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