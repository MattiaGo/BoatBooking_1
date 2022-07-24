package com.example.boatbooking_1.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.databinding.FragmentMessagesBinding
import com.example.boatbooking_1.model.ChatPreview
import com.example.boatbooking_1.model.ChatPreviewAdapter
import com.example.boatbooking_1.model.User

/**
 * A simple [Fragment] subclass.
 * Use the [Messages.newInstance] factory method to
 * create an instance of this fragment.
 */

class MessagesFragment : Fragment() {
    // View Binding
    private lateinit var binding: FragmentMessagesBinding

    private lateinit var chatPreviewRecyclerView: RecyclerView
    private lateinit var chatPreviewList: ArrayList<ChatPreview>

    // Fake data to test RecyclerView
    lateinit var userList: Array<User>
    lateinit var lastMessageList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userList = arrayOf(
            User("Admin", "admin@boatbooking.com", "0"),
            User("Matteo", "matteo@mail.com", "1"),
            User("Mattia", "mattia@mail.com", "2")
        )

        lastMessageList = arrayOf(
            "Ciao, come stai?",
            "È possibile effettuare una prenotazione?",
            "Mi dispiace non è più disponibile!"
        )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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

        chatPreviewList = arrayListOf()
        getFakeData()

        return binding.root
    }

    private fun getFakeData() {
        for (i in userList.indices) {
            val chatPreview = ChatPreview(userList[i], lastMessageList[i])
            chatPreviewList.add(chatPreview)
        }

        chatPreviewRecyclerView.adapter = ChatPreviewAdapter(chatPreviewList)
    }
}