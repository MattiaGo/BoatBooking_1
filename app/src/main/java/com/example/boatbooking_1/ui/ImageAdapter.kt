package com.example.boatbooking_1.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boatbooking_1.R
import com.example.boatbooking_1.utils.Util


class ImageAdapter(
    private val context: Context,
    private val imageList: ArrayList<String>,
    val newImageList: ArrayList<String>,
    val remoteImageList: ArrayList<String>
) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    var remoteImageToRemove: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_add_image, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = imageList[position]

        if (currentItem.startsWith("https")) {
            Log.d("Adapter", "Edit > $currentItem")
            Glide.with(context)
                .load(currentItem)
                .into(holder.imageView)
        } else {
            Log.d("Adapter", "New > $currentItem")
            holder.imageView.setImageURI(Uri.parse(currentItem))
        }

        holder.btnRemove.setOnClickListener {
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        if (position < remoteImageList.size)
        {
            remoteImageToRemove.add(remoteImageList[position])
            remoteImageList.removeAt(position)
        }

        newImageList.remove(imageList[position])
        imageList.removeAt(position)

        Log.d("Adapter", "remoteImageList: $remoteImageList")
        Log.d("Adapter", "newImageList: $newImageList")
        Log.d("Adapter", "imageList: $imageList")

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