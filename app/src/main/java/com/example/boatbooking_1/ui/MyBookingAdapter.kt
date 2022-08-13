package com.example.boatbooking_1.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.auth.UserProfileFragmentDirections
import com.example.boatbooking_1.utils.Util
import com.example.boatbooking_1.viewmodel.MyBookingViewModel
import com.example.boatbooking_1.viewmodel.UserProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.w3c.dom.Text
import java.sql.Date

class MyBookingAdapter(
    private val bookingList: ArrayList<Booking>,
    private val context: Context,
    private val myBookingViewModel: MyBookingViewModel,
    private val userViewModel: UserProfileViewModel,

) :
    RecyclerView.Adapter<MyBookingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_my_booking, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val booking = bookingList[position]
        val shipOwnerID = booking.announcement!!.id_owner
        val sdf = Util.sdfBooking()
        holder.tvShipOwner.text = ""

        if (userViewModel.getUser().value!!.shipOwner) {
            holder.btnRemove.isVisible = false
            holder.tvShipOwner.isVisible = false
        }

        Util.fStorage.reference.child("/images/${booking.announcement!!.imageList!![0]!!}")
            .downloadUrl
            .addOnSuccessListener {
                // Got the download URL
                val downloadUri = it
                Glide.with(context)
                    .load(downloadUri.toString())
                    .into(holder.imageView)
            }
            .addOnFailureListener {
                Log.d("HomeImage", "Error: $it")
            }

        Util.mDatabase
            .child("users/$shipOwnerID")
            .child("name")
            .get()
            .addOnCompleteListener {
                holder.tvShipOwner.text = "Proprietario: ".plus(it.result.value.toString())
                Log.d("MyBooking", "ShipOwner: ${it.result.value}")
            }

        holder.tvLocation.text = booking.announcement!!.location.toString()
        holder.tvBoatName.text = booking.announcement!!.boat!!.name.toString()
        holder.tvStartDate.text = sdf.format(booking.startDate!!)
        holder.tvEndDate.text = sdf.format(booking.endDate!!)
        holder.tvPassengers.text =
            booking.announcement!!.boat!!.passengers.toString().plus(" Passeggeri")
        holder.tvCabins.text =
            booking.announcement!!.boat!!.cabins.toString().plus(" Cabine")
        holder.tvBathrooms.text =
            booking.announcement!!.boat!!.bathrooms.toString().plus(" Bagni")
        holder.tvTotal.text = booking.total.toString().plus(" €")

        if (booking.endDate!!.before(Date(System.currentTimeMillis()))) {
            holder.btnRemove.isVisible = false
        }

        holder.btnRemove.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Sei sicuro di eliminare la tua prenotazione?")
                .setMessage("")
                .setCancelable(false)
                .setNegativeButton("Annulla") { _, _ ->
                }
                .setPositiveButton("Conferma") { _, _ ->
                    myBookingViewModel.removeBookingFromDatabase(booking)
                    myBookingViewModel.decrementCounter(booking.announcement!!)
                    notifyItemRemoved(position)
                    Toast.makeText(
                        context,
                        "Prenotazione rimossa con successo!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .show()
        }

        holder.itemView.setOnClickListener { view ->
            val id: String = booking.announcement!!.id!!
            //val uid: String = currentItem.user!!.uid.toString()
            val bundle = bundleOf("id" to id)

            // Test
            Navigation.findNavController(view)
                .navigate(R.id.action_myBookingsFragment_to_announcementDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_boat)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvBoatName: TextView = itemView.findViewById(R.id.tv_boat_name)
        val tvShipOwner: TextView = itemView.findViewById(R.id.tv_shipowner)
        val tvStartDate: TextView = itemView.findViewById(R.id.tv_start_date)
        val tvEndDate: TextView = itemView.findViewById(R.id.tv_end_date)
        val tvPassengers: TextView = itemView.findViewById(R.id.tv_passengers)
        val tvCabins: TextView = itemView.findViewById(R.id.tv_cabins)
        val tvBathrooms: TextView = itemView.findViewById(R.id.tv_bathrooms)
        val tvTotal: TextView = itemView.findViewById(R.id.tv_total_price)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btn_delete)
    }
}