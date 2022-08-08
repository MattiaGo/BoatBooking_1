package com.example.boatbooking_1.ui

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.repository.AnnouncementRepository
import com.example.boatbooking_1.utils.Util


class MyAnnouncementAdapter(
    private val announcementList: ArrayList<Announcement>,
    private val context: Context
) :
    RecyclerView.Adapter<MyAnnouncementAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_my_reservation, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = announcementList[position]

        holder.tvName.text = currentItem.announce_name.toString()
        holder.tvLocation.text = currentItem.location
        holder.checkBoxAvailability.isChecked = currentItem.available

        if (holder.checkBoxAvailability.isChecked) {
            holder.tvAvailable.text = "Disponibile al pubblico"
            holder.tvAvailable.setTextColor(R.color.colorHint)
        } else {
            holder.tvAvailable.text = "Non disponibile al pubblico"
            holder.tvAvailable.setTextColor(R.color.error)
        }

        var downloadUri: Uri?

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Caricamento immagini...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        if (!currentItem.imageList.isNullOrEmpty()) {
            Util.fStorage.reference.child("/images/${currentItem.imageList!!.last()}")
                .downloadUrl
                .addOnCompleteListener {
                    // Got the download URL
                    if (it.isSuccessful) {
                        downloadUri = it.result
                        Log.d("Adapter", "$downloadUri")

                        Glide.with(context)
                            .load(downloadUri.toString())
                            .into(holder.ivBoat)
                    }

                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
                .addOnFailureListener {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    Log.d("Adapter", "Error: $it")
                }
        } else {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }

        holder.itemView.setOnClickListener { view ->
            val id: String = currentItem.id.toString()
            //val uid: String = currentItem.user!!.uid.toString()
            val bundle = bundleOf("id" to id)

            // Test
            Navigation.findNavController(view).navigate(
                R.id.action_myAnnouncementsFragment_to_editAnnouncementFragment,
                bundle
            )
        }

        holder.checkBoxAvailability.setOnClickListener {
            changeAvailability(holder.checkBoxAvailability.isChecked, currentItem.id.toString())

            if (holder.checkBoxAvailability.isChecked) {
                holder.tvAvailable.text = "Disponibile al pubblico"
                holder.tvAvailable.setTextColor(R.color.colorHint)
            } else {
                holder.tvAvailable.text = "Non disponibile al pubblico"
                holder.tvAvailable.setTextColor(R.color.error)
            }
        }
    }

    private fun changeAvailability(available: Boolean, id: String) {
        AnnouncementRepository.instance.changeAvailability(available, id)
    }

    override fun getItemCount(): Int {
        return announcementList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvAvailable: TextView = itemView.findViewById(R.id.tv_available)
        val checkBoxAvailability: CheckBox = itemView.findViewById(R.id.check_box_availability)
        val ivBoat: ImageView = itemView.findViewById(R.id.iv_boat)
    }

}
