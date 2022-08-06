package com.example.boatbooking_1.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boatbooking_1.R
import com.example.boatbooking_1.utils.Util


class PublicImageAdapter(
    private val context: Context,
    private val imageList: ArrayList<String>
) :
    RecyclerView.Adapter<PublicImageAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_details_image, parent, false)

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

        holder.layoutImg.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_announcementDetailsFragment_to_zoomImageFragment)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_image)
        val layoutImg: LinearLayout = itemView.findViewById(R.id.layout_image)

    }
}