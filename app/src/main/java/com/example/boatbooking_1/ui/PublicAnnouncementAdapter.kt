package com.example.boatbooking_1.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.repository.AnnouncementRepository
import com.example.boatbooking_1.viewmodel.AnnouncementViewModel


class PublicAnnouncementAdapter(private val announcementList: ArrayList<Announcement>) :
    RecyclerView.Adapter<PublicAnnouncementAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_boat_snippet, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = announcementList[position]

        holder.tvName.text = currentItem.announce_name.toString()
        holder.tvLocation.text = currentItem.location

        holder.itemView.setOnClickListener { view ->
            val id: String = currentItem.id.toString()
            //val uid: String = currentItem.user!!.uid.toString()
            val bundle = bundleOf("id" to id)

            // Test
            Navigation.findNavController(view).navigate(R.id.action_main_home_to_announcementDetailsFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return announcementList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
    }

}
