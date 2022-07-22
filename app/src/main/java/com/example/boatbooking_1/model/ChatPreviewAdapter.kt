package com.example.boatbooking_1.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R

class ChatPreviewAdapter(private val chatPreviewList: ArrayList<ChatPreview>) :
    RecyclerView.Adapter<ChatPreviewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_preview_message, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = chatPreviewList[position]

        holder.tvName.text = currentItem.user!!.name
        holder.tvLastMessage.text = currentItem.lastMessage
    }

    override fun getItemCount(): Int {
        return chatPreviewList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvLastMessage: TextView = itemView.findViewById(R.id.tv_last_message)

    }
}