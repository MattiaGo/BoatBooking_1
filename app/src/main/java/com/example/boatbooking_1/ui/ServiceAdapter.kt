package com.example.boatbooking_1.ui

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.BoatService
import com.google.android.material.textfield.TextInputEditText

class ServiceAdapter(private val serviceList: ArrayList<BoatService>) :
    RecyclerView.Adapter<ServiceAdapter.MyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_boat_service, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = serviceList[position]

        holder.btnRemove.setOnClickListener {
            serviceList.removeAt(position)
            notifyDataSetChanged()
        }

        holder.etService.setText(currentItem.name)
        holder.etPrice.setText(currentItem.price.toString())

        holder.etService.addTextChangedListener {
            serviceList[position].name = holder.etService.text.toString()
//            Log.d("ServiceAdapter", serviceList[position].name.toString())
        }

        holder.etPrice.addTextChangedListener {
            serviceList[position].price = holder.etService.text.toString().toInt()
//            Log.d("ServiceAdapter", serviceList[position].price.toString())
        }

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etService: TextInputEditText = itemView.findViewById(R.id.et_service)
        val etPrice: TextInputEditText = itemView.findViewById(R.id.et_price)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btn_remove)
    }
}