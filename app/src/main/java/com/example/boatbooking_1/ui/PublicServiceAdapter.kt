package com.example.boatbooking_1.ui

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.BoatService
import com.google.android.material.textfield.TextInputEditText

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

        holder.etService.setText(currentItem.name)
        holder.etPrice.setText(currentItem.price.toString())
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etService: TextView = itemView.findViewById(R.id.tv_service)
        val etPrice: TextView = itemView.findViewById(R.id.tv_price)
    }
}