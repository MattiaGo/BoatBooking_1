package com.example.boatbooking_1.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Announcement
import com.example.boatbooking_1.model.Booking

class MyBookingAdapter(
    private val bookingList: ArrayList<Booking>,
    private val context: Context
) :
    RecyclerView.Adapter<MyBookingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_my_booking, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.itemView.setOnClickListener {
//            val id: String = booking.id.toString()
            //val uid: String = currentItem.user!!.uid.toString()

            val bundle = bundleOf("booking" to booking)

            // Test
            Navigation.findNavController(it).navigate(
                R.id.action_myBookingsFragment_to_userProfile,
                bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        val tvBoatName: TextView = itemView.findViewById(R.id.tv_boat_name)
        val tvShipOwner: TextView = itemView.findViewById(R.id.tv_shipowner)
        val tvStartDate: TextView = itemView.findViewById(R.id.tv_start_date)
        val tvPassengers: TextView = itemView.findViewById(R.id.tv_passengers)
        val tvCabins: TextView = itemView.findViewById(R.id.tv_cabins)
        val tvBathrooms: TextView = itemView.findViewById(R.id.tv_bathrooms)
    }
}