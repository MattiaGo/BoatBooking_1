package com.example.boatbooking_1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.BoatService
import com.example.boatbooking_1.viewmodel.BookingViewModel

class BookingServiceAdapter(
    private val boatServiceList: ArrayList<BoatService>,
    private var bookingViewModel: BookingViewModel
) :
    RecyclerView.Adapter<BookingServiceAdapter.MyViewHolder>() {

//    val servicesPrice: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_booking_service, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val service = boatServiceList[position]

        holder.tvName.text = service.name
        holder.tvPrice.text = service.price

        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                bookingViewModel.updateBookingTotal(service.price!!.toInt())
                bookingViewModel.updateBoatServiceList(service, toAdd = true)
//                bookingViewModel.setBookingTotal(bookingViewModel.getBooking().value!!.total)
//                Log.d("BookingService", service.price!!.toInt().toString())
                Log.d("BookingService", "ViewModel TOTAL: ${bookingViewModel.getBooking().value!!.total}")
            }
            else {
                bookingViewModel.updateBookingTotal(-service.price!!.toInt())
                bookingViewModel.updateBoatServiceList(service, toAdd = false)
//                bookingViewModel.setBookingTotal(bookingViewModel.getBooking().value!!.total)
//                Log.d("BookingService", service.price!!.toInt().toString())
                Log.d("BookingService", "ViewModel TOTAL: ${bookingViewModel.getBooking().value!!.total}")
            }
        }

        if (holder.checkBox.isChecked)
            Log.d("BookingService", "CheckBox [$position]: ${holder.checkBox.isChecked}")

    }

    override fun getItemCount(): Int {
        return boatServiceList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_service_name)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val checkBox: CheckBox = itemView.findViewById(R.id.check_box_service)
    }
}