package com.example.boatbooking_1.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.boatbooking_1.R
import com.example.boatbooking_1.model.Booking
import com.example.boatbooking_1.ui.MainActivity
import com.example.boatbooking_1.ui.NotificationBookingActivity
import com.example.boatbooking_1.utils.Util
import kotlin.coroutines.coroutineContext


class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = 0

    override fun onReceive(context: Context, intent: Intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
//            val fooString = intent.getStringExtra("KEY_FOO_STRING")
        Log.d("MyService", "AlarmReceiver: DEBUG")

//        Log.d("MyService", Util.getUID().toString())

        if (!Util.getUID().isNullOrBlank()) {
            Log.d("MyService", "DEBUG")
            showNotification(context)
        }
//        Toast.makeText(context, intent.getStringExtra("notification"), Toast.LENGTH_SHORT).show()

    }

    private fun showNotification(activity: Context) {
        var notificationManager: NotificationManager? = null
        val name = "Booking Notification"
        val id = "booking_notification"

        if (notificationManager == null) {
            notificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notificationManager.getNotificationChannel(id)

            if (mChannel == null) {
                mChannel = NotificationChannel(id, name, importance)
                mChannel.enableVibration(true)
                notificationManager.createNotificationChannel(mChannel)
            }

        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(activity, id)


        Util.fDatabase.collection("BoatBookings")
            .document(Util.getUID()!!)
            .collection("Booking")
            .get()
            .addOnSuccessListener {
                it.forEachIndexed { i, document ->
                    val booking = document.toObject(Booking::class.java)
                    val timestamp = booking.startDate!!.time
                    val requestCode = System.currentTimeMillis().toInt()

                    val intent = Intent(activity, NotificationBookingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra("id", booking.id)
//                    intent.putExtra("id_notification", CHANNEL_ID + i)

                    val pendingIntent: PendingIntent? = TaskStackBuilder.create(activity).run {
                        // Add the intent, which inflates the back stack
                        addNextIntentWithParentStack(intent)
                        // Get the PendingIntent containing the entire back stack
                        getPendingIntent(
                            requestCode,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                    }
//                        PendingIntent.getActivity(activity, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

                    if (DateUtils.isToday(timestamp)) {

                        builder.setContentTitle("Prenotazione#${booking.id!!.substringAfter("@")}")
                            .setSmallIcon(R.drawable.ic_rudder)
                            .setContentText("${booking.announcement!!.announce_name}")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setTicker("Boat Booking")
                            .setVibrate(longArrayOf())
                            .setStyle(
                                NotificationCompat.BigTextStyle()
                                    .bigText("Hai una prenotazione per oggi\n$booking")
                            )

//                        val dismissIntent = Intent(activity, NotificationBookingActivity::class.java).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        }
////                        dismissIntent.action = "VIEW_BOOKING"
////                        dismissIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//
//                        val dismissPendingIntent: PendingIntent? = TaskStackBuilder.create(activity).run {
//                            // Add the intent, which inflates the back stack
//                            addNextIntentWithParentStack(dismissIntent)
//                            // Get the PendingIntent containing the entire back stack
//                            getPendingIntent(requestCode,
//                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//                        }
//
//                        val bundle = Bundle()
//                        bundle.putString("id", booking.id)
//                        dismissIntent.putExtras(bundle)

//                        val dismissAction = NotificationCompat.Action(
//                            null,
//                            "VISUALIZZA",
//                            dismissPendingIntent
//                        )

//                        builder.addAction(dismissAction)

                        val notification = builder.build()

                        notificationManager.notify(CHANNEL_ID, notification)
                    }

                }
            }
    }

}