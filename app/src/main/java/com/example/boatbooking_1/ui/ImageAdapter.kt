package com.example.boatbooking_1.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.firebase.ui.auth.AuthUI.getApplicationContext
import java.io.File


class ImageAdapter(private val imageList: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_add_image, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = imageList[position]

        holder.imageView.setImageURI(Uri.parse(currentItem))

        holder.btnRemove.setOnClickListener {
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        imageList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_image)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btn_remove_image)
    }
}