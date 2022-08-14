package com.example.boatbooking_1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.BoatService

class PublicServiceAdapter(private val serviceList: ArrayList<BoatService>) :
    RecyclerView.Adapter<PublicServiceAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_public_boat_service, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = serviceList[position]

        holder.etService.text = currentItem.name
        holder.etPrice.text = currentItem.price.toString()
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etService: TextView = itemView.findViewById(R.id.tv_service)
        val etPrice: TextView = itemView.findViewById(R.id.tv_price)
    }
}