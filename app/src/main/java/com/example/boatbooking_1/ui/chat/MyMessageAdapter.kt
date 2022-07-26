package com.example.boatbooking_1.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.MyMessage
import com.google.firebase.auth.FirebaseAuth

private const val ITEM_RECEIVED = 1
private const val ITEM_SENT = 2

class MyMessageAdapter(private val messageList: ArrayList<MyMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) { // RECEIVED
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_received_message, parent, false)
            return ReceivedViewHolder(itemView)
        } else { // SENT
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_sent_message, parent, false)
            return SentViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        } else {
            holder as ReceivedViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.tv_sent_message)!!
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.tv_received_message)!!
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
//        val currentUID = FirebaseAuth.getInstance().currentUser?.uid

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderID)) {
            ITEM_SENT
        } else {
            ITEM_RECEIVED
        }
    }

}