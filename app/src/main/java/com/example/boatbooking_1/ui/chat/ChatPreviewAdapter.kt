package com.example.boatbooking_1.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.ChatPreview


class ChatPreviewAdapter(private val chatPreviewList: ArrayList<ChatPreview>) :
    RecyclerView.Adapter<ChatPreviewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_preview_message, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = chatPreviewList[position]

        holder.tvName.text = currentItem.user!!.name.toString()
        holder.tvLastMessage.text = currentItem.lastMessage.toString()

        holder.itemView.setOnClickListener { view ->
            val uid: String = currentItem.user!!.uid.toString()
            val bundle = bundleOf("uid" to uid, "name" to currentItem.user!!.name.toString())
            Navigation.findNavController(view).navigate(R.id.action_main_messages_to_chatFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return chatPreviewList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tv_last_message)

    }

}
